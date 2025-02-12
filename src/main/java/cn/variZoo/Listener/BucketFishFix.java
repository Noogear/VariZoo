package cn.variZoo.Listener;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Main;
import cn.variZoo.Util.EntityUtil;
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

import java.util.EnumSet;
import java.util.List;
import java.util.WeakHashMap;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG;

public class BucketFishFix implements Listener {
    private final NamespacedKey scaleKey;
    private final EnumSet<Material> fishBucket;
    private final WeakHashMap<Location, Double> fishScale;
    private final Attribute scaleAttribute;

    public BucketFishFix(Main main) {
        scaleKey = new NamespacedKey(main, "scale");
        this.fishBucket = EnumSet.copyOf((List.of(
                Material.AXOLOTL_BUCKET,
                Material.COD_BUCKET,
                Material.SALMON_BUCKET,
                Material.PUFFERFISH_BUCKET,
                Material.TROPICAL_FISH_BUCKET,
                Material.TADPOLE_BUCKET
        )));
        fishScale = new WeakHashMap<>();
        scaleAttribute = EntityUtil.getScaleAttribute();
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
                    AttributeInstance scale = ((LivingEntity) entity).getAttribute(scaleAttribute);
                    if (scale == null) return;
                    scale.setBaseValue(sca);
                    if (Config.other.effectHealth) {
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
            AttributeInstance scale = entity.getAttribute(scaleAttribute);
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
        if (scale == 1) return;
        Block block = event.getBlock();
        int y = 0;
        if (block.getBlockData() instanceof Waterlogged) {
            y = 1;
        }
        fishScale.put(block.getLocation().add(0.5, y, 0.5), scale);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDispenser(BlockDispenseEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.DISPENSER) return;
        if (!fishBucket.contains(event.getItem().getType())) return;
        ItemStack bucket = event.getItem();
        PersistentDataContainer pdc = bucket.getItemMeta().getPersistentDataContainer();
        double scale = pdc.getOrDefault(scaleKey, PersistentDataType.DOUBLE, 1.0);
        if (scale == 1) return;
        BlockFace facing = ((Directional) block.getBlockData()).getFacing();
        Block targetBlock = block.getRelative(facing);
        int y = 0;
        if (targetBlock.getBlockData() instanceof Waterlogged) {
            y = 1;
        }
        fishScale.put(block.getLocation().add(0.5, y, 0.5), scale);
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
