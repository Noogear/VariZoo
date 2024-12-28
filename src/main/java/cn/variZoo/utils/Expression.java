package cn.variZoo.utils;

import cn.variZoo.Configuration;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {
    public static Expression instance;

    private final EvaluationEnvironment breedFinalScaleEnv = new EvaluationEnvironment();
    private final EvaluationEnvironment breedHurtEnv = new EvaluationEnvironment();
    private final EvaluationEnvironment increaseDropsEnv = new EvaluationEnvironment();
    private String breedFinalExpression;
    private String breedHurtExpression;
    private String increaseDropsExpression;

    public Expression() {
        instance = this;
        load();
    }

    public static void load() {
        try {

            instance.breedFinalScaleEnv.setVariableNames(instance.setEnv(Configuration.Breed.inheritance.finalScale));
            instance.breedFinalExpression = Configuration.Breed.inheritance.finalScale.replaceAll(" ","").replaceAll("\\{([^}]*)}", "$1");

            instance.breedHurtEnv.setVariableNames(instance.setEnv(Configuration.Breed.multiple.hurt));
            instance.breedHurtExpression = Configuration.Breed.multiple.hurt.replaceAll(" ","").replaceAll("\\{([^}]*)}", "$1");

            instance.increaseDropsEnv.setVariableNames(instance.setEnv(Configuration.other.increaseDrops));
            instance.increaseDropsExpression = Configuration.other.increaseDrops.replaceAll(" ","").replaceAll("\\{([^}]*)}", "$1");

        } catch (Exception e) {
            XLogger.err("Failed to load expression configuration: %s", e.getMessage());
        }
    }

    public static double evaluateBreedFinalScale(double father, double mother, double degree) {
        return Crunch.compileExpression(instance.breedFinalExpression, instance.breedFinalScaleEnv).evaluate(father, mother, degree);
    }

    public static double evaluateBreedHurt(double maxHealth, double health) {
        return Crunch.compileExpression(instance.breedHurtExpression, instance.breedHurtEnv).evaluate(maxHealth, health);
    }

    public static double evaluateIncreaseDrops(double scale) {
        return Crunch.compileExpression(instance.increaseDropsExpression, instance.increaseDropsEnv).evaluate(scale);
    }

    private String[] setEnv(String expression) {
        Pattern pattern = Pattern.compile("\\{([^}]*)}");
        Matcher matcher = pattern.matcher(expression);

        List<String> formattedTexts = new ArrayList<>();
        while (matcher.find()) {
            formattedTexts.add(matcher.group(1));
        }

        return formattedTexts.toArray(String[]::new);
    }


}
