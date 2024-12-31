package cn.variZoo.utils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Degree {

    private final double start;
    private final double end;
    private final double[] fixed;

    public Degree(double start, double end, double[] fixed) {
        this.start = start;
        this.end = end;
        this.fixed = fixed;
    }

    public static Degree build(String value) {
        double start = 0;
        double end = 0;
        double[] fixed = new double[0];
        if (value.contains("-")) {
            String[] range = value.replaceAll(" ", "").split("-");
            if (range.length >= 2) {
                start = Double.parseDouble(range[0]);
                end = Double.parseDouble(range[range.length - 1]);
            }
        } else {
            fixed = Arrays.stream(value.replaceAll(" ", "").split(",")).mapToDouble(Double::parseDouble).toArray();
        }
        return new Degree(start, end, fixed);
    }

    public double getRandom() {
        if (fixed.length == 0) {
            return ThreadLocalRandom.current().nextDouble(start, end);
        }
        return fixed[ThreadLocalRandom.current().nextInt(fixed.length)];
    }
}
