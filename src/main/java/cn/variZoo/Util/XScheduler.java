package cn.variZoo.Util;

import cn.variZoo.Main;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.TimeUnit;

public class XScheduler {

    public static XScheduler instance;
    private final IScheduler scheduler;

    public XScheduler(Main main, Boolean isFolia) {

        instance = this;
        if (isFolia) {
            this.scheduler = new FoliaScheduler(main);
        } else {
            this.scheduler = new CommonScheduler(main);
        }

    }

    public static IScheduler get() {
        return instance.scheduler;
    }

    public interface IScheduler {

        void cancelAll();

        /**
         * Run a task later
         *
         * @param task  The task to run
         * @param delay The delay in ticks (20 ticks = 1 second)
         */
        void runTaskLater(Runnable task, long delay);

        /**
         * Run a task
         *
         * @param task The task to run
         */
        void runTask(Runnable task);

        /**
         * Run a task later asynchronously
         *
         * @param task  The task to run
         * @param delay The delay in milliseconds
         */
        void runTaskLaterAsync(Runnable task, long delay);

        /**
         * Run a task asynchronously
         *
         * @param task The task to run
         */
        void runTaskAsync(Runnable task);

    }

    private static class CommonScheduler implements IScheduler {
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

    private static class FoliaScheduler implements IScheduler {
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

}
