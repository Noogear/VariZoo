package cn.variZoo.listeners;

import cn.variZoo.Configuration;
import cn.variZoo.Main;
import cn.variZoo.utils.EntityUtil;
import io.papermc.paper.entity.Bucketable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG;

public class BucketFishFix implements Listener {
    private final Main plugin;
    private final NamespacedKey scaleKey;
    private final Set<Material> fishBucket;
    private final WeakHashMap<Location, Double> fishScale;

    public BucketFishFix(Main main) {
        this.plugin = main;
        scaleKey = new NamespacedKey(plugin, "scale");
        this.fishBucket = new HashSet<>(List.of(
                Material.AXOLOTL_BUCKET,
                Material.COD_BUCKET,
                Material.SALMON_BUCKET,
                Material.PUFFERFISH_BUCKET,
                Material.TROPICAL_FISH_BUCKET,
                Material.TADPOLE_BUCKET
        ));
        fishScale = new WeakHashMap<>();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onFishSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Bucketable entity) {
            if (event.getSpawnReason() == SPAWNER_EGG) {
                Location loc = entity.getLocation();
                loc.setYaw(0);
                double sca = getFishScale(loc);
                if (sca != 0) {
                    entity.setFromBucket(true);
                    EntityUtil.setInvalid(entity);
                    AttributeInstance scale = ((LivingEntity) entity).getAttribute(EntityUtil.getScaleAttribute());
                    if (scale == null) return;
                    scale.setBaseValue(sca);
                    if (Configuration.other.effectHealth) {
                        AttributeInstance maxHealth = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        if (maxHealth == null) return;
                        maxHealth.setBaseValue(maxHealth.getBaseValue() * sca);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketFish(PlayerBucketEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            AttributeInstance scale = entity.getAttribute(EntityUtil.getScaleAttribute());
            if (scale == null) return;
            ItemStack bucket = event.getEntityBucket();
            ItemMeta meta = bucket.getItemMeta();
            meta.getPersistentDataContainer().set(scaleKey, PersistentDataType.DOUBLE, scale.getValue());
            bucket.setItemMeta(meta);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!fishBucket.contains(event.getBucket())) return;
        ItemStack bucket = event.getPlayer().getInventory().getItem(event.getHand());
        PersistentDataContainer pdc = bucket.getItemMeta().getPersistentDataContainer();
        double scale = pdc.getOrDefault(scaleKey, PersistentDataType.DOUBLE, 1.0);
        if (scale == 1.0) return;
        Block block = event.getBlock();
        int y = 0;
        if (block.getBlockData() instanceof Waterlogged) {
            y = 1;
        }
        Location loc = block.getLocation().add(0.5, y, 0.5);
        fishScale.put(loc, scale);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDispenser(BlockDispenseEvent event) {
        if (event.getBlock().getType() != Material.DISPENSER) return;
        if (!fishBucket.contains(event.getItem().getType())) return;
        ItemStack bucket = event.getItem();
        PersistentDataContainer pdc = bucket.getItemMeta().getPersistentDataContainer();
        double scale = pdc.getOrDefault(scaleKey, PersistentDataType.DOUBLE, 1.0);
        if (scale == 1.0) return;
        BlockFace facing = ((Directional) event.getBlock().getBlockData()).getFacing();
        Block block = event.getBlock().getRelative(facing);
        int y = 0;
        if (block.getBlockData() instanceof Waterlogged) {
            y = 1;
        }
        Location loc = block.getLocation().add(0.5, y, 0.5);
        fishScale.put(loc, scale);
    }

    private double getFishScale(Location loc) {
        if (fishScale.containsKey(loc)) {
            Double scale = fishScale.get(loc);
            fishScale.remove(loc);
            return scale;
        }
        return 0;
    }
}
