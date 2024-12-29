package cn.variZoo.managers;

import cn.variZoo.Configuration;
import cn.variZoo.utils.Degree;
import cn.variZoo.utils.XLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DegreeManager {

    private final Map<String, Degree> finalDegrees = new HashMap<>();

    public DegreeManager() {
        load();
    }

    public void load() {
        Map<String, String> degrees = new HashMap<>();
        degrees.put("animalBasicDegree", Configuration.AnimalSpawn.basic.degree);
        degrees.put("animalMutantDegree", Configuration.AnimalSpawn.mutant.degree);
        degrees.put("breedInheritanceDegree", Configuration.Breed.inheritance.degree);

        try {
            finalDegrees.clear();
            degrees.forEach((key, value) -> {
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
                finalDegrees.put(key, new Degree(start, end, fixed));
            });
        } catch (Exception e) {
            XLogger.err("Failed to load degree configuration: " + e.getMessage());
        }

    }

    public double getDegree(String degree) {
        if (finalDegrees.containsKey(degree)) {
            return finalDegrees.get(degree).getRandom();
        }
        return 0;
    }
}
