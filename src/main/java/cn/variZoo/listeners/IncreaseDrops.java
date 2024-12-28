package cn.variZoo.listeners;

import cn.variZoo.utils.EntityUtil;
import cn.variZoo.utils.Expression;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IncreaseDrops implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEvent(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Animals entity)) return;
        double scale = entity.getAttribute(EntityUtil.getScaleAttribute()).getValue();
        if (scale == 1) return;
        double increase = Expression.evaluateIncreaseDrops(scale);
        List<ItemStack> drops = event.getDrops();
        for (ItemStack drop : drops) {
            drop.setAmount((int) Math.round(drop.getAmount() * increase));
        }
    }

}
