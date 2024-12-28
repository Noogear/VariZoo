package cn.variZoo.listeners;

import cn.variZoo.Configuration;
import cn.variZoo.Main;
import cn.variZoo.managers.ConfigManager;
import cn.variZoo.utils.Expression;
import cn.variZoo.utils.Scheduler;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.concurrent.ThreadLocalRandom;

public class AnimalBreed implements Listener {
    private final Main plugin;

    public AnimalBreed(Main main) {
        this.plugin = main;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;

        if (isInvalidBreed(entity, event.getBreeder())) return;

        Animals mother = (Animals) event.getMother();
        Animals father = (Animals) event.getFather();

        double degree = plugin.degreeManager.getDegree("breedInheritanceDegree");

        double birthScale = Expression.evaluateBreedFinalScale(
                father.getAttribute(Attribute.GENERIC_SCALE).getValue(),
                mother.getAttribute(Attribute.GENERIC_SCALE).getValue(),
                degree
        );

        AttributeInstance babyScale = entity.getAttribute(Attribute.GENERIC_SCALE);

        if (babyScale != null) {
            babyScale.setBaseValue(birthScale * babyScale.getValue());
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

        if (plugin.configManager.breedMultipleHurt) {
            father.damage(Expression.evaluateBreedHurt(father.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), father.getHealth()));
            mother.damage(Expression.evaluateBreedHurt(mother.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), mother.getHealth()));
        }

    }


    private boolean isInvalidBreed(Animals e, LivingEntity p) {
        ConfigManager config = plugin.configManager;
        if (config.breedBlacklistWorld.contains(e.getWorld().getName())) return true;
        if (config.breedBlacklistAnimal.contains(e.getType())) return true;
        if (p instanceof Player) {
            return p.hasPermission("varizoo.skip.breed");
        }
        return false;
    }
}