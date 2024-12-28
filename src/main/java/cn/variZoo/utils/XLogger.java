package cn.variZoo.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class XLogger {
    public static XLogger instance;
    private final Logger logger;

    public XLogger() {
        instance = this;
        this.logger = Logger.getLogger("VariZoo");
    }

    public XLogger(@Nullable JavaPlugin plugin) {
        instance = this;
        this.logger = plugin != null ? plugin.getLogger() : Logger.getLogger("VariZoo");
    }

    public static void info(String message) {
        instance.logger.info(" I | " + message);
    }

    public static void info(String message, Object... args) {
        instance.logger.info(" I | " + String.format(message, args));
    }

    public static void warn(String message) {
        instance.logger.warning(" W | " + message);
    }

    public static void warn(String message, Object... args) {
        instance.logger.warning(" W | " + String.format(message, args));
    }

    public static void err(String message) {
        instance.logger.severe(" E | " + message);
    }

    public static void err(String message, Object... args) {
        instance.logger.severe(" E | " + String.format(message, args));
    }
}
