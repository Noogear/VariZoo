package cn.variZoo.Util;

import cn.variZoo.Configuration.File.Language;
import cn.variZoo.Util.Scheduler.XScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Message {
    public static Message instance;
    private Component help;

    public Message() {
        instance = this;
        load();
    }

    public static void load() {
        instance.help = buildMsg(Language.help);
    }

    public static Component buildMsg(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component buildMsg(List<String> messages) {
        StringBuilder msg = new StringBuilder();
        for (String message : messages) {
            msg.append(message).append("\n");
        }
        int length = msg.length();
        if (length > 0 && msg.charAt(length - 1) == '\n') {
            msg.deleteCharAt(length - 1);
        }
        return MiniMessage.miniMessage().deserialize(msg.toString());

    }

    public static void sendMsg(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    public static void sendMsg(CommandSender sender, String message) {
        XScheduler.get().runTaskAsync(() -> {
            sender.sendMessage(buildMsg(message));
        });
    }

    public static void sendMsg(CommandSender sender, String message, Object... args) {
        XScheduler.get().runTaskAsync(() -> {
            sender.sendMessage(buildMsg(String.format(message, args)));
        });
    }

    public static void showHelp(CommandSender sender) {
        sendMsg(sender, instance.help);

    }


}
