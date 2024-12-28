package cn.variZoo.managers;

import cn.variZoo.Configuration;
import cn.variZoo.Main;
import cn.variZoo.listeners.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {
    private final Main plugin;
    private final PluginManager pluginManager;

    public ListenerManager(Main main) {
        this.plugin = main;
        pluginManager = plugin.getServer().getPluginManager();
        load();
    }

    private void load() {
        if (!Configuration.enabled) return;

        if (Configuration.AnimalSpawn.basic.apply > 0) {
            pluginManager.registerEvents(new AnimalSpawn(plugin), plugin);
        }

        if (!Configuration.Breed.inheritance.finalScale.isEmpty()) {
            pluginManager.registerEvents(new AnimalBreed(plugin), plugin);
        }

        if (Configuration.other.bucketFishFix) {
            pluginManager.registerEvents(new BucketFishFix(plugin), plugin);
        }

        if (!Configuration.other.increaseDrops.isEmpty()) {
            pluginManager.registerEvents(new IncreaseDrops(), plugin);
        }

        if (Configuration.other.transform) {
            pluginManager.registerEvents(new AnimalTransform(), plugin);
        }
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        load();
    }


}
