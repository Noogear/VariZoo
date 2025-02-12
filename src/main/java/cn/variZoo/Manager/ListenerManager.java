package cn.variZoo.Manager;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Listener.*;
import cn.variZoo.Main;
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
        if (!Config.enabled) return;

        if (Config.AnimalSpawn.basic.apply > 0) {
            pluginManager.registerEvents(new AnimalSpawn(), plugin);
        }

        if (!Config.Breed.inheritance.finalScale.isEmpty()) {
            pluginManager.registerEvents(new AnimalBreed(), plugin);
        }

        if (Config.other.bucketFishFix) {
            pluginManager.registerEvents(new BucketFishFix(plugin), plugin);
        }

        if (!Config.other.increaseDrops.isEmpty()) {
            pluginManager.registerEvents(new IncreaseDrops(), plugin);
        }

        if (Config.other.transform) {
            pluginManager.registerEvents(new AnimalTransform(), plugin);
        }
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        load();
    }


}
