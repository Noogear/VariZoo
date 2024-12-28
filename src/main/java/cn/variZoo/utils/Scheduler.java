package cn.variZoo.utils;

import cn.variZoo.Main;

import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static Scheduler instance;
    private final boolean isFolia;
    private final Main plugin;

    public Scheduler(Main main) {
        this.plugin = main;
        instance = this;
        isFolia = main.isFolia();
    }

    public static void cancelAll() {
        if (instance.isFolia) {
            instance.plugin.getServer().getGlobalRegionScheduler().cancelTasks(instance.plugin);
            instance.plugin.getServer().getGlobalRegionScheduler().cancelTasks(instance.plugin);
        } else {
            instance.plugin.getServer().getScheduler().cancelTasks(instance.plugin);
        }
    }

    /**
     * Run a task later
     *
     * @param task  The task to run
     * @param delay The delay in ticks (20 ticks = 1 second)
     */
    public static void runTaskLater(Runnable task, long delay) {
        if (delay <= 0) {
            runTask(task);
            return;
        }
        if (instance.isFolia) {
            instance.plugin.getServer().getGlobalRegionScheduler().runDelayed(instance.plugin, (plugin) -> task.run(), delay);
        } else {
            instance.plugin.getServer().getScheduler().runTaskLater(instance.plugin, task, delay);
        }
    }

    /**
     * Run a task
     *
     * @param task The task to run
     */
    public static void runTask(Runnable task) {
        if (instance.isFolia) {
            instance.plugin.getServer().getGlobalRegionScheduler().run(instance.plugin, (plugin) -> task.run());
        } else {
            instance.plugin.getServer().getScheduler().runTask(instance.plugin, task);
        }
    }



}
