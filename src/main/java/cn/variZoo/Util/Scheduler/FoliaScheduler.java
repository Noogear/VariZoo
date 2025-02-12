package cn.variZoo.Util.Scheduler;

import cn.variZoo.Main;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;

import java.util.concurrent.TimeUnit;

public class FoliaScheduler implements IScheduler {
    private final Main plugin;
    private final GlobalRegionScheduler scheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaScheduler(Main main) {
        this.plugin = main;
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
        scheduler = plugin.getServer().getGlobalRegionScheduler();
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
        scheduler.runDelayed(plugin, (plugin) -> task.run(), delay);
    }

    @Override
    public void runTask(Runnable task) {
        scheduler.run(plugin, (plugin) -> task.run());
    }

    @Override
    public void runTaskLaterAsync(Runnable task, long delay) {
        if (delay <= 0) {
            runTaskAsync(task);
            return;
        }
        asyncScheduler.runDelayed(plugin, (plugin) -> task.run(), delay * 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void runTaskAsync(Runnable task) {
        asyncScheduler.runNow(plugin, (plugin) -> task.run());
    }
}
