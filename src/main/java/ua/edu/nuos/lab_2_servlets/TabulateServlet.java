package ua.edu.nuos.lab_2_servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "tabulateServlet", value = "/tabulate-servlet")
public class TabulateServlet extends HttpServlet {

    private final Tabulation tabulation = new Tabulation();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        double a, b, h;
        try {
            a = Double.parseDouble(request.getParameter("a").trim().replace(',', '.'));
            b = Double.parseDouble(request.getParameter("b").trim().replace(',', '.'));
            h = Double.parseDouble(request.getParameter("h").trim().replace(',', '.'));
        } catch (RuntimeException ex) {
            printError(out, "Некоректні дані: a, b, h мають бути числами.");
            return;
        }
        if (h <= 0) {
            printError(out, "Крок h має бути більше 0.");
            return;
        }
        if (a > b) {
            printError(out, "Має виконуватись a ≤ b.");
            return;
        }
        long count = Tabulation.countPoints(a, b, h);
        if (count > Tabulation.MAX_POINTS) {
            printError(out, "Занадто багато точок (" + count + "). Максимум — " + Tabulation.MAX_POINTS + ".");
            return;
        }

        List<Tabulation.Point> points = tabulation.tabulate(a, b, h);

        out.println("<html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><div class=\"container\">");
        out.println("<h1>Завдання 2.2. Табулювання y = x^0.3</h1>");
        out.println("<p>Відрізок [" + num(a) + "; " + num(b) + "], крок h = " + num(h)
                + ", точок: " + points.size() + "</p>");

        out.println("<h2>Таблиця значень</h2>");
        out.println("<table>");
        out.println("<tr><th>№</th><th>x</th><th>y</th></tr>");
        int i = 1;
        for (Tabulation.Point p : points) {
            String y = Double.isNaN(p.y()) ? "не визначена" : String.format(Locale.ROOT, "%.6f", p.y());
            out.println("<tr><td>" + i++ + "</td><td>" + num(p.x()) + "</td><td>" + y + "</td></tr>");
        }
        out.println("</table>");

        out.println("<h2>Графік</h2>");
        out.println(SvgGraph.build(points, a, b));

        out.println("<p><a href=\"index.jsp\">На головну</a></p>");
        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void printError(PrintWriter out, String message) {
        out.println("<html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><div class=\"container\">");
        out.println("<h1>Помилка</h1>");
        out.println("<p>" + message + "</p>");
        out.println("<p><a href=\"index.jsp\">Повернутися до форми</a></p>");
        out.println("</div></body></html>");
    }

    // число без зайвих нулів
    private String num(double v) {
        return new BigDecimal(v).round(new MathContext(10)).stripTrailingZeros().toPlainString();
    }
}