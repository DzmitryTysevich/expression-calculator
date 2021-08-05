import lexemanalyzer.Analyzer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@WebServlet("/calc")
public class servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        Map<String, String[]> parameters = req.getParameterMap();
        String[] expression = getExpression(req);
        String expressionWithValue = getExpressionWithValue(parameters, expression);

        Analyzer analyzer = new Analyzer();
        pw.println(analyzer.getExpressionResult(expressionWithValue, req));
    }

    private String[] getExpression(HttpServletRequest req) {
        return req.getParameter("expression").split("");
    }

    private String getExpressionWithValue(Map<String, String[]> parameters, String[] expression) {
        ArrayList<String> listExpression = new ArrayList<>(List.of(expression));
        IntStream.range(0, listExpression.size())
                .filter(i -> parameters.containsKey(listExpression.get(i)))
                .forEach(i -> listExpression.set(i, String.join("", parameters.get(listExpression.get(i)))));
        return String.join("", listExpression);
    }
}