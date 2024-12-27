package cn.scalingZoo.listeners;

import io.papermc.paper.entity.Bucketable;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.concurrent.ThreadLocalRandom;

public class AnimalSpawn implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;

        if (entity instanceof Bucketable b) {
            if (b.isFromBucket()) return;
        }

        int mutant = ThreadLocalRandom.current().nextInt(100);
        if (mutant > 33) return;

        AttributeInstance scale = entity.getAttribute(Attribute.GENERIC_SCALE);
        if (scale == null) return;
        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;

        double randomScale = ThreadLocalRandom.current().nextDouble(0.86, 1.16);

        if (mutant == 0) {
            randomScale *= 1.3;
            entity.getWorld().spawnParticle(Particle.GLOW_SQUID_INK, entity.getLocation(), 15);
        } else if (mutant == 33) {
            randomScale *= 0.77;
            entity.getWorld().spawnParticle(Particle.GLOW, entity.getLocation(), 15);
        }
        double finalScale = Math.max(.00625, Math.min(16, randomScale * scale.getValue()));
        scale.setBaseValue(finalScale);
        maxHealth.setBaseValue(Math.max(3, randomScale * maxHealth.getValue()));
        entity.setHealth(maxHealth.getValue());
    }


}
