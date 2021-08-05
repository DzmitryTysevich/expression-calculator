import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/calc")
public class servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String expression = req.getParameter("expression");
        PrintWriter pw = resp.getWriter();
        switch (expression) {
            case "a+b/c": {
                int a = Integer.parseInt(req.getParameter("a"));
                int b = Integer.parseInt(req.getParameter("b"));
                int c = Integer.parseInt(req.getParameter("c"));
                pw.println(a + b / c);
                break;
            }
            case "x+y/z":
                int x = Integer.parseInt(req.getParameter("x"));
                int y = Integer.parseInt(req.getParameter("y"));
                String z = req.getParameter("z");
                if (isDigit(z)) {
                    pw.println(x + y / Integer.parseInt(z));
                } else {
                    pw.println(x + y / Integer.parseInt(req.getParameter(z)));
                }
                break;
            case "(f + k)*(h - g)/f":
                int f = Integer.parseInt(req.getParameter("f"));
                int k = Integer.parseInt(req.getParameter("k"));
                int h = Integer.parseInt(req.getParameter("h"));
                int g = Integer.parseInt(req.getParameter("g"));
                pw.println((f + k) * (h - g) / f);
                break;
            case "a/b/c/d": {
                int a = Integer.parseInt(req.getParameter("a"));
                int b = Integer.parseInt(req.getParameter("b"));
                int c = Integer.parseInt(req.getParameter("c"));
                int d = Integer.parseInt(req.getParameter("d"));
                pw.println(a / b / c / d);
                break;
            }
            default: {
                int a = Integer.parseInt(req.getParameter("a"));
                int b = Integer.parseInt(req.getParameter("b"));
                String c = req.getParameter("c");
                if (isDigit(c)) {
                    pw.println((Integer.parseInt(c) * (a - b) / b) * a);
                } else if (c.equals("a")) {
                    pw.println((a * (a - b) / b) * a);
                } else {
                    pw.println((b * (a - b) / b) * a);
                }
                break;
            }
        }
    }

    private boolean isDigit(String parameter) {
        return Character.isDigit(parameter.toCharArray()[0]);
    }
}