package cn.variZoo.managers;

import cn.variZoo.Command;
import cn.variZoo.Main;
import cn.variZoo.utils.XLogger;
import org.bukkit.command.PluginCommand;

public class CommandManager {
    private final Main plugin;
    private final PluginCommand mainCommand;


    public CommandManager(Main main) {
        this.plugin = main;
        mainCommand = plugin.getCommand("varizoo");
        if (mainCommand != null) {
            registerCommand();
        } else {
            XLogger.err("Failed to load command.");
        }

    }

    private void registerCommand() {
        mainCommand.setExecutor(new Command(plugin));
    }


}
