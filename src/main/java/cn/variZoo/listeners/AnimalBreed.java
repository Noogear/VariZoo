package cn.variZoo.listeners;

import cn.variZoo.Configuration;
import cn.variZoo.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import redempt.crunch.CompiledExpression;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class AnimalBreed implements Listener {

    private final CompiledExpression breedFinalScaleExpression;
    private final CompiledExpression breedHurtExpression;
    private Degree breedInheritanceDegree;
    private String breedActionbar;
    private boolean breedActionbarEnabled;
    private boolean multipleHurtEnabled;
    private Set<EntityType> blacklistEntity;
    private Set<String> blacklistWorld;

    public AnimalBreed() {

        try {
            breedInheritanceDegree = Degree.build(Configuration.Breed.inheritance.degree);
            breedActionbar = Configuration.Breed.inheritance.actionbar
                    .replace("{", "<")
                    .replace("}", ">");
            breedActionbarEnabled = !breedActionbar.isEmpty();
            multipleHurtEnabled = !Configuration.Breed.multiple.hurt.isEmpty();
            blacklistEntity = EntityUtil.entityToSet(Configuration.Breed.blackList.animal);
            blacklistWorld = new HashSet<>(Configuration.Breed.blackList.world);
        } catch (Exception e) {
            XLogger.err(e.getMessage());
        }
        breedFinalScaleExpression = DataUtil.buildExpression(Configuration.Breed.inheritance.finalScale, "father", "mother", "degree");
        breedHurtExpression = DataUtil.buildExpression(Configuration.Breed.multiple.hurt, "max_health", "health");
        
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;

        if (isInvalidBreed(entity, event.getBreeder())) return;

        Animals mother = (Animals) event.getMother();
        Animals father = (Animals) event.getFather();

        double degree = breedInheritanceDegree.getRandom();

        double birthScale = breedFinalScaleExpression.evaluate(
                father.getAttribute(EntityUtil.getScaleAttribute()).getValue(),
                mother.getAttribute(EntityUtil.getScaleAttribute()).getValue(),
                degree
        );

        AttributeInstance babyScale = entity.getAttribute(EntityUtil.getScaleAttribute());

        if (babyScale != null) {
            babyScale.setBaseValue(birthScale * babyScale.getValue());
        }

        if (Configuration.Breed.inheritance.skipAnimalSpawn) {
            EntityUtil.setInvalid(entity);
        }

        if (Configuration.other.effectHealth) {
            AttributeInstance babyHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (babyHealth == null) return;
            double finalHealth = birthScale * babyHealth.getValue();
            babyHealth.setBaseValue(finalHealth);
            entity.setHealth(finalHealth);
        }

        double multiple = Configuration.Breed.multiple.apply;
        if (multiple > 0) {
            Scheduler.runTaskLater(() -> {
                if (ThreadLocalRandom.current().nextInt(100) > multiple) return;
                mother.setLoveModeTicks(100);
                father.setLoveModeTicks(100);
            }, Configuration.Breed.multiple.delay);
        }

        if (multipleHurtEnabled) {
            father.damage(breedHurtExpression.evaluate(father.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), father.getHealth()));
            mother.damage(breedHurtExpression.evaluate(mother.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), mother.getHealth()));
        }

        if (breedActionbarEnabled) {
            if (event.getBreeder() instanceof Player p) {
                Scheduler.runTaskLater(() -> {
                    if (babyScale != null) {
                        Component actionbar = MiniMessage.miniMessage().deserialize(breedActionbar,
                                Placeholder.parsed("scale", String.format("%.2f", babyScale.getValue())),
                                Placeholder.parsed("baby", EntityUtil.getI18nName(entity)),
                                Placeholder.parsed("player", p.getName())
                        );
                        p.sendActionBar(actionbar);
                    }
                }, 1);
            }
        }
    }


    private boolean isInvalidBreed(Animals e, LivingEntity p) {
        if (blacklistWorld.contains(e.getWorld().getName())) return true;
        if (blacklistEntity.contains(e.getType())) return true;
        if (p instanceof Player player) {
            if (player.hasPermission("varizoo.skip.breed")) {
                return !player.isOp();
            }
        }
        return false;
    }
}
