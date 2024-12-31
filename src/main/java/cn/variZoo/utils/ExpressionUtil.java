package cn.variZoo.utils;

import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

public class ExpressionUtil {

    public static CompiledExpression build(String expression, String... variables) {
        try {
            EvaluationEnvironment env = new EvaluationEnvironment();
            String exp = expression.replace(" ", "").replaceAll("\\{([^}]*)}", "$1");
            env.setVariableNames(variables);
            return Crunch.compileExpression(exp, env);
        } catch (Exception e) {
            XLogger.err("Fail to build Expression: %s", e.getMessage());
            return null;
        }
    }

}
