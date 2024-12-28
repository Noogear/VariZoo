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

        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            paper = true;
        } catch (ClassNotFoundException e) {
            paper = false;
        }

        new XLogger(this);
        new Scheduler(this);
        configManager = new ConfigManager(this);
        listenerManager = new ListenerManager(this);
        degreeManager = new DegreeManager();
        commandManager = new CommandManager(this);

    }

    @Override
    public void onDisable() {

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
