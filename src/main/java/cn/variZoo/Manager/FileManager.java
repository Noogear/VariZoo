package cn.variZoo.Manager;

import cn.variZoo.Configuration.File.Config;
import cn.variZoo.Configuration.File.Language;
import cn.variZoo.Configuration.Form.ConfigurationManager;
import cn.variZoo.Main;

import java.io.File;

public class FileManager {
    private final File configFile;
    private final File languageFile;

    public FileManager(Main main) {
        configFile = new File(main.getDataFolder(), "config.yml");
        languageFile = new File(main.getDataFolder(), "language.yml");
        load();
    }

    public void load() {

        try {
            ConfigurationManager.load(Config.class, configFile, "version");
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
