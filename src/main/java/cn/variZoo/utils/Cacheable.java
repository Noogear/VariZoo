package cn.variZoo.utils;

import org.bukkit.Location;

import java.util.WeakHashMap;

public class Cacheable {
    private final WeakHashMap<Location, Double> fishScale ;
    public static Cacheable instance;

    public Cacheable() {
        instance = this;
        fishScale = new WeakHashMap<>();
    }

    public static void addFishScale(Location loc, double scale) {
        instance.fishScale.put(loc, scale);
    }

    public static double getFishScale(Location loc) {
        if (instance.fishScale.containsKey(loc)) {
            Double scale = instance.fishScale.get(loc);
            instance.fishScale.remove(loc);
            return scale;
        }
        return 0;
    }

}
