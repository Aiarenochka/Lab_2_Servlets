package ua.edu.nuos.lab_2_servlets;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;

public class SvgGraph {
    private static final int W = 720, H = 440, L = 60, R = 20, T = 20, B = 40;
    private static final int PW = W - L - R, PH = H - T - B;

    public static String build(List<Tabulation.Point> points, double a, double b) {
        double ymin = Double.POSITIVE_INFINITY, ymax = Double.NEGATIVE_INFINITY;
        int valid = 0;
        for (Tabulation.Point p : points) {
            if (!Double.isNaN(p.y())) {
                ymin = Math.min(ymin, p.y());
                ymax = Math.max(ymax, p.y());
                valid++;
            }
        }
        if (valid == 0) {
            return "<p>На заданому відрізку функція не визначена (x &lt; 0), графік побудувати неможливо.</p>";
        }

        double xmin = a, xmax = b;
        if (xmax - xmin < 1e-12) {
            xmin -= 1;
            xmax += 1;
        }
        if (ymax - ymin < 1e-12) {
            ymin -= 1;
            ymax += 1;
        }
        double pad = (ymax - ymin) * 0.05;
        ymin -= pad;
        ymax += pad;

        StringBuilder sb = new StringBuilder();
        sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(W)
                .append("\" height=\"").append(H).append("\" viewBox=\"0 0 ").append(W).append(' ').append(H)
                .append("\" style=\"border:1px solid #999;background:#fff;max-width:100%\">");

        // сітка та підписи осей
        for (int i = 0; i <= 5; i++) {
            double xv = xmin + i * (xmax - xmin) / 5;
            double yv = ymin + i * (ymax - ymin) / 5;
            double px = L + (xv - xmin) / (xmax - xmin) * PW;
            double py = T + PH - (yv - ymin) / (ymax - ymin) * PH;
            sb.append(String.format(Locale.ROOT,
                    "<line x1=\"%.1f\" y1=\"%d\" x2=\"%.1f\" y2=\"%d\" stroke=\"#ddd\"/>", px, T, px, T + PH));
            sb.append(String.format(Locale.ROOT,
                    "<line x1=\"%d\" y1=\"%.1f\" x2=\"%d\" y2=\"%.1f\" stroke=\"#ddd\"/>", L, py, L + PW, py));
            sb.append(String.format(Locale.ROOT,
                    "<text x=\"%.1f\" y=\"%d\" font-size=\"12\" text-anchor=\"middle\">%s</text>",
                    px, T + PH + 18, label(xv)));
            sb.append(String.format(Locale.ROOT,
                    "<text x=\"%d\" y=\"%.1f\" font-size=\"12\" text-anchor=\"end\">%s</text>",
                    L - 6, py + 4, label(yv)));
        }
        sb.append(String.format(Locale.ROOT,
                "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"none\" stroke=\"#333\"/>", L, T, PW, PH));

        // осі координат, якщо вони потрапляють в область графіка
        if (xmin <= 0 && xmax >= 0) {
            double px = L + (0 - xmin) / (xmax - xmin) * PW;
            sb.append(String.format(Locale.ROOT,
                    "<line x1=\"%.1f\" y1=\"%d\" x2=\"%.1f\" y2=\"%d\" stroke=\"#333\" stroke-width=\"1.5\"/>",
                    px, T, px, T + PH));
        }
        if (ymin <= 0 && ymax >= 0) {
            double py = T + PH - (0 - ymin) / (ymax - ymin) * PH;
            sb.append(String.format(Locale.ROOT,
                    "<line x1=\"%d\" y1=\"%.1f\" x2=\"%d\" y2=\"%.1f\" stroke=\"#333\" stroke-width=\"1.5\"/>",
                    L, py, L + PW, py));
        }

        // лінія графіка; у точках, де функція не визначена, лінія розривається
        StringBuilder run = new StringBuilder();
        StringBuilder dots = new StringBuilder();
        for (Tabulation.Point p : points) {
            if (Double.isNaN(p.y())) {
                flush(sb, run);
                continue;
            }
            double px = L + (p.x() - xmin) / (xmax - xmin) * PW;
            double py = T + PH - (p.y() - ymin) / (ymax - ymin) * PH;
            run.append(String.format(Locale.ROOT, "%.2f,%.2f ", px, py));
            if (points.size() <= 200) {
                dots.append(String.format(Locale.ROOT,
                        "<circle cx=\"%.2f\" cy=\"%.2f\" r=\"3\" fill=\"#c0392b\"/>", px, py));
            }
        }
        flush(sb, run);
        sb.append(dots);

        sb.append(String.format(Locale.ROOT,
                "<text x=\"%d\" y=\"%d\" font-size=\"13\" text-anchor=\"middle\">x</text>", L + PW / 2, H - 6));
        sb.append("<text x=\"14\" y=\"").append(T + PH / 2)
                .append("\" font-size=\"13\" text-anchor=\"middle\" transform=\"rotate(-90 14 ")
                .append(T + PH / 2).append(")\">y = x^0.3</text>");
        sb.append("</svg>");
        return sb.toString();
    }

    private static void flush(StringBuilder sb, StringBuilder run) {
        if (run.length() > 0) {
            sb.append("<polyline fill=\"none\" stroke=\"#2c6fbb\" stroke-width=\"2\" points=\"")
                    .append(run).append("\"/>");
            run.setLength(0);
        }
    }

    // число з 3 значущими цифрами для підписів осей
    private static String label(double v) {
        if (Math.abs(v) < 1e-12) return "0";
        return new BigDecimal(v).round(new MathContext(3)).stripTrailingZeros().toPlainString();
    }
}