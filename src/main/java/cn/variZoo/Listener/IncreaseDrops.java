package cn.variZoo.Listener;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Util.EntityUtil;
import cn.variZoo.Util.ExpressionUtil;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import redempt.crunch.CompiledExpression;

import java.util.List;

public class IncreaseDrops implements Listener {

    private final CompiledExpression increaseDropsExpression;

    public IncreaseDrops() {
        increaseDropsExpression = ExpressionUtil.build(Config.other.increaseDrops, "scale");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;
        double scale = entity.getAttribute(EntityUtil.getScaleAttribute()).getValue();
        if (scale != 1) {
            double increase = increaseDropsExpression.evaluate(scale);
            List<ItemStack> drops = event.getDrops();
            for (ItemStack drop : drops) {
                drop.setAmount((int) Math.round(drop.getAmount() * increase));
            }
        }
    }

}
