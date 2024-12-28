package cn.variZoo;

import cn.variZoo.managers.CommandManager;
import cn.variZoo.managers.ConfigManager;
import cn.variZoo.managers.DegreeManager;
import cn.variZoo.managers.ListenerManager;
import cn.variZoo.utils.Expression;
import cn.variZoo.utils.Scheduler;
import cn.variZoo.utils.XLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public ConfigManager configManager;
    public ListenerManager listenerManager;
    public DegreeManager degreeManager;
    public CommandManager commandManager;
    private boolean paper;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        new XLogger(this);
        XLogger.info(this.getServer().getName() + this.getServer().getVersion());
        XLogger.info("██╗   ██╗ █████╗ ██████╗ ██╗███████╗ ██████╗  ██████╗ ");
        XLogger.info("██║   ██║██╔══██╗██╔══██╗██║╚══███╔╝██╔═══██╗██╔═══██╗");
        XLogger.info("██║   ██║███████║██████╔╝██║  ███╔╝ ██║   ██║██║   ██║");
        XLogger.info("╚██╗ ██╔╝██╔══██║██╔══██╗██║ ███╔╝  ██║   ██║██║   ██║");
        XLogger.info(" ╚████╔╝ ██║  ██║██║  ██║██║███████╗╚██████╔╝╚██████╔╝");
        XLogger.info("  ╚═══╝  ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚══════╝ ╚═════╝  ╚═════╝ ");
        XLogger.info("Version: " + this.getDescription().getVersion());

        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            paper = true;
        } catch (ClassNotFoundException e) {
            paper = false;
        }
        configManager = new ConfigManager(this);
        listenerManager = new ListenerManager(this);
        degreeManager = new DegreeManager();
        commandManager = new CommandManager(this);
        new Scheduler(this);
        new Expression();

        long elapsedTime = System.currentTimeMillis() - startTime;
        XLogger.info("Plugin loaded successfully in " + elapsedTime + " ms");
    }

    @Override
    public void onDisable() {
        Scheduler.cancelAll();
    }

    public boolean isPaper() {
        return paper;
    }

    public void reload() {


        Scheduler.cancelAll();
        configManager.load();
        listenerManager.reload();
        degreeManager.load();
        Expression.load();


    }
}
