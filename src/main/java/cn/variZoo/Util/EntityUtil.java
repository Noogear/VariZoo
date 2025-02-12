package cn.variZoo.Util;

import cn.variZoo.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntityUtil {

    public static EntityUtil instance;
    private final Attribute scaleAttribute;
    private final NamespacedKey invalidKey;

    public EntityUtil(Main main, Attribute scaleAttribute) {
        instance = this;
        this.scaleAttribute = scaleAttribute;
        this.invalidKey = new NamespacedKey(main, "invalid");
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

    public static String getI18nName(Entity entity) {
        return entity.customName() == null ? "<translate:" + entity.getType().translationKey() + ">" : entity.getCustomName();
    }

    public static EnumSet<EntityType> entityToSet(List<String> entities) {
        return entities.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return EntityType.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + " is not a valid entity type.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(EntityType.class)));
    }

    public static EnumSet<CreatureSpawnEvent.SpawnReason> spawnReasonToSet(List<String> spawnReason) {
        return spawnReason.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return CreatureSpawnEvent.SpawnReason.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + " is not a valid spawn reason.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(CreatureSpawnEvent.SpawnReason.class)));
    }

}
