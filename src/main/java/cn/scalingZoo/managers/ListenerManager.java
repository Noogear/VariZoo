package cn.scalingZoo.managers;

import cn.scalingZoo.listeners.AnimalBreed;
import cn.scalingZoo.listeners.AnimalSpawn;
import cn.scalingZoo.listeners.BucketFishFix;
import cn.scalingZoo.main;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {
    private final main main;

    public ListenerManager(main main) {
        this.main = main;
        registerEvents();
    }

    public void registerEvents() {
        PluginManager pluginManager = main.getServer().getPluginManager();

        pluginManager.registerEvents(new AnimalSpawn(), main);
        pluginManager.registerEvents(new BucketFishFix(), main);

        pluginManager.registerEvents(new AnimalBreed(main), main);

    }


}
