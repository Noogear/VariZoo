package cn.variZoo.utils;

import cn.variZoo.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class EntityUtil {

    private final Main plugin;
    private final Attribute scaleAttribute;
    public static EntityUtil instance;
    private final NamespacedKey invalidKey;

    public EntityUtil(Main main, Attribute scaleAttribute) {
        instance = this;
        this.plugin = main;
        this.scaleAttribute = scaleAttribute;
        this.invalidKey = new NamespacedKey(plugin, "invalid");
    }

    public static Attribute getScaleAttribute() {
        return instance.scaleAttribute;
    }

    public static void setInvalid(Entity entity) {
        entity.getPersistentDataContainer().set(instance.invalidKey, PersistentDataType.BOOLEAN, true);
    }

    public static boolean isInvalid(Entity entity) {
        return entity.getPersistentDataContainer().has(instance.invalidKey);
    }

}
