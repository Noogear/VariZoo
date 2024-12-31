package cn.variZoo.utils;

import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

public class DataUtil {
    public static DataUtil instance;

    public DataUtil() {
        instance = this;
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
