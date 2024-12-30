package cn.variZoo.listeners;

import cn.variZoo.Configuration;
import cn.variZoo.utils.*;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class AnimalSpawn implements Listener {
    private final Set<EntityType> blackListEntity;
    private final Set<CreatureSpawnEvent.SpawnReason> blackListSpawnReason;
    private Degree animalBasicDegree;
    private Degree animalMutantDegree;
    private boolean mutantParticleEnabled;
    private Particle mutantparticle;
    private Set<String> blackListWorld;

    public AnimalSpawn() {

        try {
            animalBasicDegree = DataUtil.saveDegree(Configuration.AnimalSpawn.basic.degree);
            animalMutantDegree = DataUtil.saveDegree(Configuration.AnimalSpawn.mutant.degree);
            mutantparticle = Particle.valueOf(Configuration.AnimalSpawn.Mutant.particle.type.toUpperCase(Locale.ROOT));
            mutantParticleEnabled = !(Configuration.AnimalSpawn.Mutant.particle.type.isEmpty() && Configuration.AnimalSpawn.Mutant.particle.count < 1);
            blackListWorld = new HashSet<>(Configuration.AnimalSpawn.blackList.world);
        } catch (Exception e) {
            XLogger.err(e.getMessage());
        }

        blackListEntity = EntityUtil.entityToSet(Configuration.AnimalSpawn.blackList.animal);
        blackListSpawnReason = EntityUtil.spawnReasonToSet(Configuration.AnimalSpawn.blackList.spawnReason);


    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;

        if (isInvalidSpawn(entity, event.getSpawnReason())) return;

        if (EntityUtil.isInvalid(entity)) return;

        if (ThreadLocalRandom.current().nextInt(100) > Configuration.AnimalSpawn.basic.apply) return;

        AttributeInstance scale = entity.getAttribute(EntityUtil.getScaleAttribute());
        if (scale == null) return;

        double randomScale = animalBasicDegree.getRandom();

        if (ThreadLocalRandom.current().nextInt(100) < Configuration.AnimalSpawn.mutant.apply) {
            double randomMutant = animalMutantDegree.getRandom();
            if (Objects.equals(Configuration.AnimalSpawn.mutant.mode, "MORE")) {
                if ((randomScale >= 1 && randomMutant < 1) || (randomScale < 1 && randomMutant >= 1)) {
                    randomMutant = 1 / randomMutant;
                }
            }
            randomScale = randomScale * randomMutant;
            if (mutantParticleEnabled) {
                Scheduler.runTaskLater(() -> {
                    entity.getWorld().spawnParticle(mutantparticle, entity.getLocation(), Configuration.AnimalSpawn.Mutant.particle.count);
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
        if (blackListWorld.contains(e.getWorld().getName())) return true;
        if (blackListEntity.contains(e.getType())) return true;
        return blackListSpawnReason.contains(reason);
    }


}
