package cn.variZoo;

import cn.variZoo.managers.CommandManager;
import cn.variZoo.managers.ConfigManager;
import cn.variZoo.managers.DegreeManager;
import cn.variZoo.managers.ListenerManager;
import cn.variZoo.utils.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public ConfigManager configManager;
    public ListenerManager listenerManager;
    public DegreeManager degreeManager;
    public CommandManager commandManager;
    private boolean folia;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        new XLogger(this);

        boolean enabled = false;
        for (Attribute attribute : Attribute.values()) {
            if (attribute.name().toLowerCase().contains("scale")) {
                new EntityUtil(this,attribute);
                enabled = true;
            }
        }

        if (!enabled) {
            XLogger.err("This server is not supported. The plugin will be disabled!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        XLogger.info(this.getServer().getName() + this.getServer().getVersion());
        XLogger.info("██╗   ██╗ █████╗ ██████╗ ██╗███████╗ ██████╗  ██████╗ ");
        XLogger.info("██║   ██║██╔══██╗██╔══██╗██║╚══███╔╝██╔═══██╗██╔═══██╗");
        XLogger.info("██║   ██║███████║██████╔╝██║  ███╔╝ ██║   ██║██║   ██║");
        XLogger.info("╚██╗ ██╔╝██╔══██║██╔══██╗██║ ███╔╝  ██║   ██║██║   ██║");
        XLogger.info(" ╚████╔╝ ██║  ██║██║  ██║██║███████╗╚██████╔╝╚██████╔╝");
        XLogger.info("  ╚═══╝  ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚══════╝ ╚═════╝  ╚═════╝ ");
        XLogger.info("https://github.com/Noogear/VariZoo");
        XLogger.info("Version: " + this.getDescription().getVersion());

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        new  Cacheable();
        new Message();
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

    public boolean isFolia() {
        return folia;
    }

    public void reload() {
        Scheduler.cancelAll();
        configManager.load();
        listenerManager.reload();
        degreeManager.load();
        Expression.load();
        Message.load();
    }
}
