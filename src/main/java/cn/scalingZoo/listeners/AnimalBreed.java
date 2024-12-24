package cn.scalingZoo.listeners;

import cn.scalingZoo.main;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Bee;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.concurrent.ThreadLocalRandom;

public class AnimalBreed implements Listener {
    private final main main;

    public AnimalBreed(main main) {
        this.main = main;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;
        if (entity instanceof Bee) return;

        Animals mother = (Animals) event.getMother();
        Animals father = (Animals) event.getFather();

        double birthScale = (mother.getAttribute(Attribute.GENERIC_SCALE).getValue() + father.getAttribute(Attribute.GENERIC_SCALE).getValue()) / ThreadLocalRandom.current().nextDouble(1.8, 2.2);

        AttributeInstance babyScale = entity.getAttribute(Attribute.GENERIC_SCALE);
        AttributeInstance babyHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (babyScale != null) {
            babyScale.setBaseValue(birthScale * babyScale.getValue());
        }

        if (babyHealth != null) {
            double finalHealth = birthScale * babyHealth.getValue();
            babyHealth.setBaseValue(finalHealth);
            entity.setHealth(finalHealth);
        }

        Bukkit.getScheduler().runTaskLater(main.getInstance(), () -> {
            if (ThreadLocalRandom.current().nextInt(100) > 9) return;
            mother.setLoveModeTicks(100);
            father.setLoveModeTicks(100);
        }, 2L);
    }
}
