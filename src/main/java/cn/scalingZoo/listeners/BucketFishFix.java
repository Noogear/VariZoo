package cn.scalingZoo.listeners;

import cn.scalingZoo.utils.Cacheable;
import io.papermc.paper.entity.Bucketable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static cn.scalingZoo.utils.Cacheable.addFishScale;
import static cn.scalingZoo.utils.Constant.isFishBucket;
import static cn.scalingZoo.utils.Constant.scaleKey;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG;

public class BucketFishFix implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onFishSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Bucketable entity) {
            if (event.getSpawnReason() == SPAWNER_EGG) {
                Location loc = entity.getLocation();
                loc.setYaw(0);
                double sca = Cacheable.getFishScale(loc);
                if (sca != 0) {
                    entity.setFromBucket(true);
                    AttributeInstance scale = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_SCALE);
                    if (scale == null) return;
                    AttributeInstance maxHealth = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    if (maxHealth == null) return;
                    scale.setBaseValue(sca);
                    maxHealth.setBaseValue(maxHealth.getBaseValue() * sca);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFish(PlayerBucketEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            AttributeInstance scale = entity.getAttribute(Attribute.GENERIC_SCALE);
            if (scale == null) return;
            ItemStack bucket = event.getEntityBucket();
            ItemMeta meta = bucket.getItemMeta();
            meta.getPersistentDataContainer().set(scaleKey(), PersistentDataType.DOUBLE, scale.getValue());
            bucket.setItemMeta(meta);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!isFishBucket(event.getBucket())) return;
        ItemStack bucket = event.getPlayer().getInventory().getItem(event.getHand());
        PersistentDataContainer pdc = bucket.getItemMeta().getPersistentDataContainer();
        double scale = pdc.getOrDefault(scaleKey(), PersistentDataType.DOUBLE, 1.0);
        if (scale == 1.0) return;
        Block block = event.getBlock();
        Location loc = block.getLocation().add(0.5, 0, 0.5);
        if (block.getBlockData() instanceof Waterlogged) {
            loc.add(0, 1, 0);
        }
        addFishScale(loc, scale);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDispenser(BlockDispenseEvent event) {
        if (event.getBlock().getType() != Material.DISPENSER) return;
        if (!isFishBucket(event.getItem().getType())) return;
        ItemStack bucket = event.getItem();
        PersistentDataContainer pdc = bucket.getItemMeta().getPersistentDataContainer();
        double scale = pdc.getOrDefault(scaleKey(), PersistentDataType.DOUBLE, 1.0);
        if (scale == 1.0) return;
        BlockFace facing = ((Directional) event.getBlock().getBlockData()).getFacing();
        Block block = event.getBlock().getRelative(facing);
        Location loc = block.getLocation().add(0.5, 0, 0.5);
        if (block.getBlockData() instanceof Waterlogged) {
            loc.add(0, 1, 0);
        }
        addFishScale(loc, scale);
    }
}
