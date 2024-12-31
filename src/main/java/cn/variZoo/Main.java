package cn.variZoo;

import cn.variZoo.managers.FileManager;
import cn.variZoo.managers.ListenerManager;
import cn.variZoo.utils.EntityUtil;
import cn.variZoo.utils.Message;
import cn.variZoo.utils.Scheduler;
import cn.variZoo.utils.XLogger;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public FileManager fileManager;
    public ListenerManager listenerManager;
    private boolean folia;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        new XLogger(this);

        boolean enabled = false;
        for (Attribute attribute : Attribute.values()) {
            if (attribute.name().toLowerCase().contains("scale")) {
                new EntityUtil(this, attribute);
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
        fileManager = new FileManager(this);
        listenerManager = new ListenerManager(this);
        new Message();
        new Scheduler(this);

        PluginCommand mainCommand = getCommand("varizoo");
        if (mainCommand != null) {
            mainCommand.setExecutor(new Command(this));
        } else {
            XLogger.err("Failed to load command.");
        }

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
        fileManager.load();
        listenerManager.reload();
    }
}
