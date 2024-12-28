package cn.variZoo.utils;

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

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public double[] getFixed() {
        return fixed;
    }

    public double getRandom() {
        if (fixed.length == 0) {
            return ThreadLocalRandom.current().nextDouble(start, end);
        }
        return fixed[ThreadLocalRandom.current().nextInt(fixed.length)];
    }

}
