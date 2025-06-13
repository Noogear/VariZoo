package cn.variZoo.Listener;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Util.Degree;
import cn.variZoo.Util.EntityUtil;
import cn.variZoo.Util.XScheduler;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnimalSpawn implements Listener {
    private final double basicApply;
    private final double mutantApply;
    private final EnumSet<EntityType> blackListEntity;
    private final EnumSet<CreatureSpawnEvent.SpawnReason> blackListSpawnReason;
    private final XScheduler.IScheduler scheduler;
    private final Attribute scaleAttribute;
    private final Degree animalBasicDegree;
    private final Degree animalMutantDegree;
    private final boolean mutantModeIsMore;
    private final boolean mutantParticleEnabled;
    private final boolean effectHealth;
    private final Particle mutantparticle;
    private final int particleCount;
    private final Set<String> blackListWorld;
    private final double maxScale;
    private final double minScale;

    public AnimalSpawn() {
        basicApply = Config.AnimalSpawn.basic.apply;
        mutantApply = Config.AnimalSpawn.mutant.apply;
        animalBasicDegree = Degree.build(Config.AnimalSpawn.basic.degree);
        animalMutantDegree = Degree.build(Config.AnimalSpawn.mutant.degree);
        mutantModeIsMore = Objects.equals(Config.AnimalSpawn.mutant.mode, "MORE");
        mutantparticle = Particle.valueOf(Config.AnimalSpawn.Mutant.particle.type.toUpperCase(Locale.ROOT));
        particleCount = Config.AnimalSpawn.Mutant.particle.count;
        mutantParticleEnabled = Config.AnimalSpawn.Mutant.particle.type.isEmpty() || particleCount < 1;
        effectHealth = Config.other.effectHealth;
        blackListWorld = new HashSet<>(Config.AnimalSpawn.blackList.world);
        blackListEntity = EntityUtil.entityToSet(Config.AnimalSpawn.blackList.animal);
        blackListSpawnReason = EntityUtil.spawnReasonToSet(Config.AnimalSpawn.blackList.spawnReason);
        maxScale = Math.min(Config.AnimalSpawn.scaleLimit.max, 16);
        minScale = Math.max(Config.AnimalSpawn.scaleLimit.min, .00625);


        scheduler = XScheduler.get();
        scaleAttribute = EntityUtil.getScaleAttribute();
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;
        if (isInvalidSpawn(entity, event.getSpawnReason())) return;
        if (EntityUtil.isInvalid(entity)) return;
        if (ThreadLocalRandom.current().nextInt(100) > basicApply) return;

        AttributeInstance scale = entity.getAttribute(scaleAttribute);
        if (scale == null) return;

        double randomScale = animalBasicDegree.getRandom();

        if (ThreadLocalRandom.current().nextInt(100) < mutantApply) {
            double randomMutant = animalMutantDegree.getRandom();
            if (mutantModeIsMore) {
                if ((randomScale >= 1 && randomMutant < 1) || (randomScale < 1 && randomMutant >= 1)) {
                    randomMutant = 1 / randomMutant;
                }
            }
            randomScale = randomScale * randomMutant;
            if (mutantParticleEnabled) {
                scheduler.runTaskLater(() -> {
                    entity.getWorld().spawnParticle(mutantparticle, entity.getLocation(), particleCount);
                }, 1);
            }
        }

        double finalScale = Math.max(minScale, Math.min(maxScale, randomScale * scale.getValue()));
        scale.setBaseValue(finalScale);

        if (effectHealth) {
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
