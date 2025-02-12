package cn.variZoo.Listener;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Util.EntityUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class AnimalTransform implements Listener {

    private final Attribute scaleAttribute;

    public AnimalTransform() {
        scaleAttribute = EntityUtil.getScaleAttribute();
    }

    @EventHandler(ignoreCancelled = true)
    public void onTransform(EntityTransformEvent event) {
        if (!(event.getEntity() instanceof Animals from)) return;

        AttributeInstance fromScale = from.getAttribute(scaleAttribute);
        if (fromScale == null) return;

        if (!(event.getTransformedEntity() instanceof LivingEntity to)) return;
        AttributeInstance toScale = to.getAttribute(scaleAttribute);
        if (toScale == null || toScale.getValue() != 1.0) return;

        toScale.setBaseValue(fromScale.getValue());

        if (Config.other.effectHealth) {
            AttributeInstance maxHealth = to.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth == null) return;
            maxHealth.setBaseValue(Math.max(1, toScale.getValue() * maxHealth.getValue()));
            to.setHealth(maxHealth.getValue());
        }

    }

}
