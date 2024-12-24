package cn.scalingZoo;

import cn.scalingZoo.managers.ConfigManager;
import cn.scalingZoo.managers.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
    private main instance;

    public main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new ConfigManager(this);

        new ListenerManager(this);


    }

    @Override
    public void onDisable() {

    }
}
