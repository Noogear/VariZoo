package cn.variZoo.Listener;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Util.Degree;
import cn.variZoo.Util.EntityUtil;
import cn.variZoo.Util.ExpressionUtil;
import cn.variZoo.Util.Scheduler.IScheduler;
import cn.variZoo.Util.Scheduler.XScheduler;
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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class AnimalBreed implements Listener {

    private final CompiledExpression breedFinalScaleExpression;
    private final CompiledExpression breedHurtExpression;
    private final IScheduler scheduler;
    private final Attribute scaleAttribute;
    private final Degree breedInheritanceDegree;
    private final String breedActionbar;
    private final boolean breedActionbarEnabled;
    private final boolean multipleHurtEnabled;
    private final EnumSet<EntityType> blacklistEntity;
    private final Set<String> blacklistWorld;
    private final double multiple;
    private final long breedDelay;

    public AnimalBreed() {
        breedInheritanceDegree = Degree.build(Config.Breed.inheritance.degree);
        blacklistEntity = EntityUtil.entityToSet(Config.Breed.blackList.animal);
        blacklistWorld = new HashSet<>(Config.Breed.blackList.world);
        breedActionbar = Config.Breed.inheritance.actionbar
                .replace("{", "<")
                .replace("}", ">");
        breedActionbarEnabled = !breedActionbar.isEmpty();
        breedFinalScaleExpression = ExpressionUtil.build(Config.Breed.inheritance.finalScale, "father", "mother", "degree");
        multipleHurtEnabled = !Config.Breed.multiple.hurt.isEmpty();
        if (multipleHurtEnabled) {
            breedHurtExpression = ExpressionUtil.build(Config.Breed.multiple.hurt, "max_health", "health");
        } else {
            breedHurtExpression = null;
        }
        multiple = Config.Breed.multiple.apply;
        breedDelay = Config.Breed.multiple.delay;

        scheduler = XScheduler.get();
        scaleAttribute = EntityUtil.getScaleAttribute();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;

        if (isInvalidBreed(entity, event.getBreeder())) return;

        Animals mother = (Animals) event.getMother();
        Animals father = (Animals) event.getFather();

        double degree = breedInheritanceDegree.getRandom();

        double birthScale = breedFinalScaleExpression.evaluate(
                father.getAttribute(scaleAttribute).getValue(),
                mother.getAttribute(scaleAttribute).getValue(),
                degree
        );

        AttributeInstance babyScale = entity.getAttribute(scaleAttribute);

        if (babyScale != null) {
            babyScale.setBaseValue(birthScale * babyScale.getValue());
        }

        if (Config.Breed.inheritance.skipAnimalSpawn) {
            EntityUtil.setInvalid(entity);
        }

        if (Config.other.effectHealth) {
            AttributeInstance babyHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (babyHealth == null) return;
            double finalHealth = birthScale * babyHealth.getValue();
            babyHealth.setBaseValue(finalHealth);
            entity.setHealth(finalHealth);
        }

        if (multiple > 0) {
            scheduler.runTaskLater(() -> {
                if (ThreadLocalRandom.current().nextInt(100) > multiple) return;
                mother.setLoveModeTicks(100);
                father.setLoveModeTicks(100);
            }, breedDelay);
        }

        if (multipleHurtEnabled) {
            father.damage(breedHurtExpression.evaluate(father.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), father.getHealth()));
            mother.damage(breedHurtExpression.evaluate(mother.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), mother.getHealth()));
        }

        if (breedActionbarEnabled) {
            if (event.getBreeder() instanceof Player p) {
                scheduler.runTaskLater(() -> {
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
