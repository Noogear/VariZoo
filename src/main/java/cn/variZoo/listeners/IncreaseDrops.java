package cn.variZoo.listeners;

import cn.variZoo.Configuration;
import cn.variZoo.utils.EntityUtil;
import cn.variZoo.utils.XLogger;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.List;

public class IncreaseDrops implements Listener {

    private CompiledExpression increaseDropsExpression;

    public IncreaseDrops() {
        try {
            String expression = Configuration.Breed.multiple.hurt.replace(" ", "").replaceAll("\\{([^}]*)}", "$1");
            EvaluationEnvironment env = new EvaluationEnvironment();
            env.setVariableNames("scale");
            increaseDropsExpression = Crunch.compileExpression(expression, env);
            increaseDropsExpression.evaluate(1);
        } catch (Exception e) {
            XLogger.err(e.getMessage());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEvent(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;
        double scale = entity.getAttribute(EntityUtil.getScaleAttribute()).getValue();
        if (scale == 1) return;
        double increase = increaseDropsExpression.evaluate(scale);
        List<ItemStack> drops = event.getDrops();
        for (ItemStack drop : drops) {
            drop.setAmount((int) Math.round(drop.getAmount() * increase));
        }
    }

}
