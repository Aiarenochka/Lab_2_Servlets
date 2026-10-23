package ua.edu.nuos.lab_2_servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@WebServlet(name = "seriesServlet", value = "/series-servlet")
public class SeriesServlet extends HttpServlet {

    private final SeriesCalculator calculator = new SeriesCalculator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        double x, e;
        int n;
        try {
            x = Double.parseDouble(request.getParameter("x").trim().replace(',', '.'));
            e = Double.parseDouble(request.getParameter("e").trim().replace(',', '.'));
            n = Integer.parseInt(request.getParameter("n").trim());
        } catch (RuntimeException ex) {
            printError(out, "Некоректні дані: x і e мають бути числами, n — цілим числом.");
            return;
        }
        if (Math.abs(x) >= 1) {
            printError(out, "x має належати інтервалу (-1; 1), бо радіус збіжності R = 1.");
            return;
        }
        if (n < 1 || n > 10_000_000) {
            printError(out, "n має бути цілим числом від 1 до 10 000 000.");
            return;
        }
        if (e <= 0) {
            printError(out, "e має бути більше 0.");
            return;
        }

        double sumN = calculator.calculateSumByN(x, n);
        double exact = calculator.calculateExact(x);
        double e2 = e / 10; // друге значення e відрізняється на порядок
        SeriesCalculator.SumResult r1 = calculator.calculateSumByEps(x, e);
        SeriesCalculator.SumResult r2 = calculator.calculateSumByEps(x, e2);

        out.println("<html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><div class=\"container\">");
        out.println("<h1>Завдання 2.1. Ряд для 1/(1+x)^2</h1>");
        out.println("<p>x = " + fmt(x) + ", n = " + n + ", e = " + sci(e) + "</p>");

        out.println("<h2>a) Сума n доданків</h2>");
        out.println("<p>S(" + n + ") = " + fmt(sumN) + "</p>");

        out.println("<h2>b) Сума доданків, більших за модулем за e</h2>");
        out.println("<table>");
        out.println("<tr><th>e</th><th>Кількість доданків</th><th>Сума</th></tr>");
        out.println("<tr><td>" + sci(e) + "</td><td>" + r1.count() + "</td><td>" + fmt(r1.sum()) + "</td></tr>");
        out.println("<tr><td>" + sci(e2) + "</td><td>" + r2.count() + "</td><td>" + fmt(r2.sum()) + "</td></tr>");
        out.println("</table>");

        out.println("<h2>c) Порівняння з точним значенням</h2>");
        out.println("<p>1 / (1 + x)^2 = " + fmt(exact) + "</p>");
        out.println("<table>");
        out.println("<tr><th>Спосіб</th><th>Наближене значення</th><th>Абсолютна похибка</th></tr>");
        printRow(out, "Сума " + n + " доданків", sumN, exact);
        printRow(out, "Доданки &gt; " + sci(e), r1.sum(), exact);
        printRow(out, "Доданки &gt; " + sci(e2), r2.sum(), exact);
        out.println("</table>");

        out.println("<p><a href=\"index.jsp\">На головну</a></p>");
        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void printRow(PrintWriter out, String name, double approx, double exact) {
        out.println("<tr><td>" + name + "</td><td>" + fmt(approx) + "</td><td>"
                + sci(Math.abs(exact - approx)) + "</td></tr>");
    }

    private void printError(PrintWriter out, String message) {
        out.println("<html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><div class=\"container\">");
        out.println("<h1>Помилка</h1>");
        out.println("<p>" + message + "</p>");
        out.println("<p><a href=\"index.jsp\">Повернутися до форми</a></p>");
        out.println("</div></body></html>");
    }

    private String fmt(double v) {
        return String.format(Locale.ROOT, "%.12f", v);
    }

    private String sci(double v) {
        return String.format(Locale.ROOT, "%.3e", v);
    }
}