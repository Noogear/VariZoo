package cn.variZoo;

import cn.variZoo.Manager.FileManager;
import cn.variZoo.Manager.ListenerManager;
import cn.variZoo.Util.*;
import cn.variZoo.Util.Scheduler.XScheduler;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public FileManager fileManager;
    public ListenerManager listenerManager;

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

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            new XScheduler(this,true);
        } catch (ClassNotFoundException e) {
            new XScheduler(this,false);
        }
        fileManager = new FileManager(this);

        listenerManager = new ListenerManager(this);
        new Message();

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
        XScheduler.get().cancelAll();
    }

    public void reload() {
        XScheduler.get().cancelAll();
        fileManager.load();
        listenerManager.reload();
        Message.load();
    }
}
