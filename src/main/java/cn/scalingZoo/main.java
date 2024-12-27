package cn.scalingZoo;

import cn.scalingZoo.managers.ConfigManager;
import cn.scalingZoo.managers.ListenerManager;
import cn.scalingZoo.utils.configuration.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class main extends JavaPlugin {
    private main instance;

    public main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        File configFile = new File(this.getDataFolder(), "config.yml");
        try {
            ConfigurationManager.load(Configuration.class, configFile, "version");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new ConfigManager();
        new ListenerManager(this);


    }

    @Override
    public void onDisable() {

    }
}
