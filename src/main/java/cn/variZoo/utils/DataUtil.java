package cn.variZoo.utils;

import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

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

    public static CompiledExpression buildExpression(String expression, String... variables) {
        try {
            String exp = expression.replace(" ", "").replaceAll("\\{([^}]*)}", "$1");
            EvaluationEnvironment env = new EvaluationEnvironment();
            env.setVariableNames(variables);
            CompiledExpression finalExpression = Crunch.compileExpression(exp, env);
            double[] doubleArray = new double[variables.length];
            for (int i = 0; i < variables.length; i++) {
                doubleArray[i] = 1.0;
            }
            finalExpression.evaluate(doubleArray);
            return finalExpression;
        } catch (Exception e) {
            XLogger.err("Fail to build Expression: %s", e.getMessage());
            return null;
        }

    }

}
