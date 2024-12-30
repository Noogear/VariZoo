package cn.variZoo.managers;

import cn.variZoo.Configuration;
import cn.variZoo.Main;
import cn.variZoo.utils.configuration.ConfigurationManager;

import java.io.File;

public class ConfigManager {
    private final File configFile;
    private final Main plugin;

    public ConfigManager(Main main) {
        this.plugin = main;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        load();

    }

    public void load() {

        try {
            ConfigurationManager.load(Configuration.class, configFile, "version");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
