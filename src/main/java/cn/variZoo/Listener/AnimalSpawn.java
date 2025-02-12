package cn.variZoo.Listener;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Util.Degree;
import cn.variZoo.Util.EntityUtil;
import cn.variZoo.Util.Scheduler.IScheduler;
import cn.variZoo.Util.Scheduler.XScheduler;
import cn.variZoo.Util.XLogger;
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
    private final IScheduler scheduler;
    private Degree animalBasicDegree;
    private Degree animalMutantDegree;
    private boolean mutantModeIsMore;
    private boolean mutantParticleEnabled;
    private Particle mutantparticle;
    private Set<String> blackListWorld;

    public AnimalSpawn() {

        try {
            animalBasicDegree = Degree.build(Config.AnimalSpawn.basic.degree);
            animalMutantDegree = Degree.build(Config.AnimalSpawn.mutant.degree);
            mutantModeIsMore = Objects.equals(Config.AnimalSpawn.mutant.mode, "MORE");
            mutantparticle = Particle.valueOf(Config.AnimalSpawn.Mutant.particle.type.toUpperCase(Locale.ROOT));
            mutantParticleEnabled = !(Config.AnimalSpawn.Mutant.particle.type.isEmpty() && Config.AnimalSpawn.Mutant.particle.count < 1);
            blackListWorld = new HashSet<>(Config.AnimalSpawn.blackList.world);
        } catch (Exception e) {
            XLogger.err(e.getMessage());
        }

        blackListEntity = EntityUtil.entityToSet(Config.AnimalSpawn.blackList.animal);
        blackListSpawnReason = EntityUtil.spawnReasonToSet(Config.AnimalSpawn.blackList.spawnReason);
        scheduler = XScheduler.get();

    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;
        if (isInvalidSpawn(entity, event.getSpawnReason())) return;
        if (EntityUtil.isInvalid(entity)) return;
        if (ThreadLocalRandom.current().nextInt(100) > Config.AnimalSpawn.basic.apply) return;

        AttributeInstance scale = entity.getAttribute(EntityUtil.getScaleAttribute());
        if (scale == null) return;

        double randomScale = animalBasicDegree.getRandom();

        if (ThreadLocalRandom.current().nextInt(100) < Config.AnimalSpawn.mutant.apply) {
            double randomMutant = animalMutantDegree.getRandom();
            if (mutantModeIsMore) {
                if ((randomScale >= 1 && randomMutant < 1) || (randomScale < 1 && randomMutant >= 1)) {
                    randomMutant = 1 / randomMutant;
                }
            }
            randomScale = randomScale * randomMutant;
            if (mutantParticleEnabled) {
                scheduler.runTaskLater(() -> {
                    entity.getWorld().spawnParticle(mutantparticle, entity.getLocation(), Config.AnimalSpawn.Mutant.particle.count);
                }, 1);
            }
        }

        double finalScale = Math.max(.00625, Math.min(16, randomScale * scale.getValue()));
        scale.setBaseValue(finalScale);

        if (Config.other.effectHealth) {
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
