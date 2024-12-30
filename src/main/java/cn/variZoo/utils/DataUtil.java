package cn.variZoo.utils;

import java.util.Arrays;

public class DataUtil {
    public static DataUtil instance;

    public DataUtil() {
        instance = this;
    }

    public static Degree saveDegree(String value) {
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


}
