package lexemanalyzer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class Analyzer {
    public int getExpressionResult(String expression, HttpServletRequest req) {
        List<Lexeme> lexemes = lexemeAnalyze(expression, req);
        return expr(new LexemeBuffer(lexemes));
    }

    private List<Lexeme> lexemeAnalyze(String expression, HttpServletRequest req) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int position = 0;
        while (position < expression.length()) {
            char character = expression.charAt(position);
            switch (character) {
                case '(':
                    lexemes.add(new Lexeme(LexemeType.LEFT_BRACKET, character));
                    position++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(LexemeType.RIGHT_BRACKET, character));
                    position++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(LexemeType.OPERATOR_PLUS, character));
                    position++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(LexemeType.OPERATOR_MINUS, character));
                    position++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(LexemeType.OPERATOR_MUL, character));
                    position++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(LexemeType.OPERATOR_DIV, character));
                    position++;
                    continue;
                default:
                    if (character <= '9' && character >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(character);
                            position++;
                            if (position >= expression.length()) {
                                break;
                            }
                            character = expression.charAt(position);
                        } while (character <= '9' && character >= '0');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else if (character <= 'z' && character >= 'a') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(req.getParameter(String.valueOf(character)));
                            position++;
                            if (position >= expression.length()) {
                                break;
                            }
                            character = expression.charAt(position);
                        } while (character <= 'z' && character >= 'a');
                        lexemes.add(new Lexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (character != ' ') {
                            throw new RuntimeException("Unexpected character: " + character);
                        }
                        position++;
                    }
            }
        }
        lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return lexemes;
    }
//    expr : plusminus* EOF ;
//    plusminus: multdiv ( ( '+' | '-' ) multdiv )* ;
//    multdiv : factor ( ( '*' | '/' ) factor )* ;
//    factor : NUMBER | '(' expr ')' ;

    private int expr(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return plusminus(lexemes);
        }
    }

    private int plusminus(LexemeBuffer lexemes) {
        int value = multdiv(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OPERATOR_PLUS:
                    value += multdiv(lexemes);
                    break;
                case OPERATOR_MINUS:
                    value -= multdiv(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private int multdiv(LexemeBuffer lexemes) {
        int value = factor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.type) {
                case OPERATOR_MUL:
                    value *= factor(lexemes);
                    break;
                case OPERATOR_DIV:
                    value /= factor(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case OPERATOR_PLUS:
                case OPERATOR_MINUS:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private int factor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.type) {
            case NUMBER:
                return Integer.parseInt(lexeme.value);
            case LEFT_BRACKET:
                int value = plusminus(lexemes);
                lexeme = lexemes.next();
                if (lexeme.type != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + lexeme.value
                            + " at position: " + lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.value
                        + " at position: " + lexemes.getPos());
        }
    }
}