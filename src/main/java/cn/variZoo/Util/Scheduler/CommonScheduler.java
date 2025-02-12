package cn.variZoo.Util.Scheduler;

import cn.variZoo.Main;
import org.bukkit.scheduler.BukkitScheduler;

public class CommonScheduler implements IScheduler {
    private final Main plugin;
    private final BukkitScheduler scheduler;

    public CommonScheduler(Main main) {
        this.plugin = main;
        scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public void cancelAll() {
        scheduler.cancelTasks(plugin);
    }

    @Override
    public void runTaskLater(Runnable task, long delay) {
        if (delay <= 0) {
            runTask(task);
            return;
        }
        scheduler.runTaskLater(plugin, task, delay);
    }

    @Override
    public void runTask(Runnable task) {
        scheduler.runTask(plugin, task);
    }


    @Override
    public void runTaskLaterAsync(Runnable task, long delay) {
        if (delay <= 0) {
            runTaskAsync(task);
            return;
        }
        scheduler.runTaskLaterAsynchronously(plugin, task, delay);
    }

    @Override
    public void runTaskAsync(Runnable task) {
        scheduler.runTaskAsynchronously(plugin, task);
    }
}
