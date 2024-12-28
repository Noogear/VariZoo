package cn.variZoo.listeners;

import cn.variZoo.Configuration;
import cn.variZoo.Main;
import cn.variZoo.managers.ConfigManager;
import cn.variZoo.utils.EntityUtil;
import cn.variZoo.utils.Scheduler;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class AnimalSpawn implements Listener {
    private final Main plugin;

    public AnimalSpawn(Main main) {
        this.plugin = main;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;

        if (isInvalidSpawn(entity, event.getSpawnReason())) return;

        if (EntityUtil.isInvalid(entity)) return;

        if (ThreadLocalRandom.current().nextInt(100) > Configuration.AnimalSpawn.basic.apply) return;

        AttributeInstance scale = entity.getAttribute(EntityUtil.getScaleAttribute());
        if (scale == null) return;

        double randomScale = plugin.degreeManager.getDegree("animalBasicDegree");

        if (ThreadLocalRandom.current().nextInt(100) < Configuration.AnimalSpawn.mutant.apply) {
            double randomMutant = plugin.degreeManager.getDegree("animalMutantDegree");
            if (Objects.equals(Configuration.AnimalSpawn.mutant.mode, "MORE")) {
                if ((randomScale >= 1 && randomMutant < 1) || (randomScale < 1 && randomMutant >= 1)) {
                    randomMutant = 1 / randomMutant;
                }
            }
            randomScale = randomScale * randomMutant;
            if (plugin.configManager.animalSpawnMutantParticle) {
                Scheduler.runTaskLater(() -> {
                    entity.getWorld().spawnParticle(Particle.valueOf(Configuration.AnimalSpawn.Mutant.particle.type), entity.getLocation(), Configuration.AnimalSpawn.Mutant.particle.count);
                }, 1);
            }
        }

        double finalScale = Math.max(.00625, Math.min(16, randomScale * scale.getValue()));
        scale.setBaseValue(finalScale);

        if (Configuration.other.effectHealth) {
            AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth == null) return;
            maxHealth.setBaseValue(Math.max(1, randomScale * maxHealth.getValue()));
            entity.setHealth(maxHealth.getValue());
        }

    }

    private boolean isInvalidSpawn(Entity e, CreatureSpawnEvent.SpawnReason reason) {
        ConfigManager config = plugin.configManager;
        if (config.animalSpawnBlacklistWorld.contains(e.getWorld().getName())) return true;
        if (config.animalSpawnBlacklistAnimal.contains(e.getType())) return true;
        return config.animalSpawnBlacklistSpawnReason.contains(reason.name());
    }


}
