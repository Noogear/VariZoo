package cn.variZoo.utils;

import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

public class ExpressionUtil {
    public static ExpressionUtil instance;
    private EvaluationEnvironment env;

    public ExpressionUtil() {
        instance = this;
        instance.env = new EvaluationEnvironment();
    }

    public static CompiledExpression build(String expression, String... variables) {
        try {
            String exp = expression.replace(" ", "").replaceAll("\\{([^}]*)}", "$1");
            instance.env.setVariableNames(variables);
            return Crunch.compileExpression(exp, instance.env);
        } catch (Exception e) {
            XLogger.err("Fail to build Expression: %s", e.getMessage());
            return null;
        }
    }

}
