package cn.variZoo.utils;

import org.bukkit.Location;

import java.util.WeakHashMap;

public class Cacheable {
    private static WeakHashMap<Location, Double> fishScale;

    public Cacheable() {
        fishScale = new WeakHashMap<>();
    }

    public static void addFishScale(Location loc, double scale) {
        fishScale.put(loc, scale);
    }

    public static double getFishScale(Location loc) {
        if (fishScale.containsKey(loc)) {
            Double scale = fishScale.get(loc);
            fishScale.remove(loc);
            return scale;
        }
        return 0;
    }

}
