package cn.variZoo.managers;

import cn.variZoo.Configuration;
import cn.variZoo.Language;
import cn.variZoo.Main;
import cn.variZoo.utils.configuration.ConfigurationManager;

import java.io.File;

public class FileManager {
    private final File configFile;
    private final File languageFile;
    private final Main plugin;

    public FileManager(Main main) {
        this.plugin = main;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        languageFile = new File(plugin.getDataFolder(), "language.yml");
        load();
    }

    public void load() {

        try {
            ConfigurationManager.load(Configuration.class, configFile, "version");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            ConfigurationManager.load(Language.class, languageFile, "version");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
