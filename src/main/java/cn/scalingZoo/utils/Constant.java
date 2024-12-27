package cn.scalingZoo.utils;

import cn.scalingZoo.main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constant {
    private static NamespacedKey scale;
    private static NamespacedKey skip;
    private static Set<Material> fishBucket;

    public Constant(main main) {
        scale = new NamespacedKey(main, "scale");
        skip = new NamespacedKey(main, "skip");
        fishBucket = Stream.of(
                Material.AXOLOTL_BUCKET,
                Material.COD_BUCKET,
                Material.SALMON_BUCKET,
                Material.PUFFERFISH_BUCKET,
                Material.TROPICAL_FISH_BUCKET,
                Material.TADPOLE_BUCKET
        ).collect(Collectors.toSet());
    }

    public static NamespacedKey scaleKey() {
        return scale;
    }

    public static NamespacedKey skipKey() {
        return skip;
    }

    public static boolean isFishBucket(Material material) {
        return fishBucket.contains(material);
    }
}
