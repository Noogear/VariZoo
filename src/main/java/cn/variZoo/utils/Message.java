package cn.variZoo.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    public static void help(CommandSender sender) {
        String message = "VariZoo help\n" +
                "/varizoo reload 重启插件";
        send(sender, message);

    }

    public static void send(CommandSender sender, String message) {
        if (sender instanceof Player p) {
            p.sendMessage(message);
        } else {
            String[] parts = message.split("\\n");
            for (String part : parts) {
                XLogger.info(part);
            }
        }
    }
}
