package cn.variZoo.utils;

import cn.variZoo.Main;

public class Scheduler {
    public static Scheduler instance;
    private final boolean isPaper;
    private final Main plugin;

    public Scheduler(Main main) {
        this.plugin = main;
        instance = this;
        isPaper = main.isPaper();
    }

    public static void cancelAll() {
        if (instance.isPaper) {
            instance.plugin.getServer().getGlobalRegionScheduler().cancelTasks(instance.plugin);
            instance.plugin.getServer().getGlobalRegionScheduler().cancelTasks(instance.plugin);
        } else {
            instance.plugin.getServer().getScheduler().cancelTasks(instance.plugin);
        }
    }

    public static void runTask(Runnable task) {
        if (instance.isPaper) {
            instance.plugin.getServer().getGlobalRegionScheduler().run(instance.plugin, (plugin) -> task.run());
        } else {
            instance.plugin.getServer().getScheduler().runTask(instance.plugin, task);
        }
    }

    public static void runTaskLater(Runnable task, long delay) {
        if (delay <= 0) {
            runTask(task);
            return;
        }
        if (instance.isPaper) {
            instance.plugin.getServer().getGlobalRegionScheduler().runDelayed(instance.plugin, (plugin) -> task.run(), delay);
        } else {
            instance.plugin.getServer().getScheduler().runTaskLater(instance.plugin, task, delay);
        }
    }


}
