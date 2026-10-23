package ua.edu.nuos.lab_2_servlets;

public class SeriesCalculator {

    public record SumResult(double sum, int count) {
    }

    public double calculateSumByN(double x, int n) {
        double sum = 0.0;
        double currentTerm = 1.0;
        for (int i = 1; i <= n; i++) {
            sum += currentTerm;
            currentTerm = currentTerm * (-x * (i + 1.0) / i);
        }
        return sum;
    }

    public SumResult calculateSumByEps(double x, double eps) {
        double sum = 0.0;
        int count = 0;
        double currentTerm = 1.0;
        for (int i = 1; i < 10_000_000; i++) {
            double nextTerm = currentTerm * (-x * (i + 1.0) / i);
            if (Math.abs(currentTerm) > eps) {
                sum += currentTerm;
                count++;
            } else if (Math.abs(nextTerm) <= Math.abs(currentTerm)) {
                break;
            }
            currentTerm = nextTerm;
        }
        return new SumResult(sum, count);
    }

    public double calculateExact(double x) {
        return 1.0 / ((1 + x) * (1 + x));
    }
}