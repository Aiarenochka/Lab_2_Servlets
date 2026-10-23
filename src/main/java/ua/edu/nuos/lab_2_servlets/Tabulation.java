package ua.edu.nuos.lab_2_servlets;

import java.util.ArrayList;
import java.util.List;

public class Tabulation {
    public static final int MAX_POINTS = 5000;

    public record Point(double x, double y) {
    }

    public static double function(double x) {
        return Math.pow(x, 0.3);
    }

    public static long countPoints(double a, double b, double h) {
        return (long) Math.floor((b - a) / h + 1e-9) + 1;
    }

    public List<Point> tabulate(double a, double b, double h) {
        int n = (int) countPoints(a, b, h);
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double x = a + i * h;
            points.add(new Point(x, function(x)));
        }
        return points;
    }
}