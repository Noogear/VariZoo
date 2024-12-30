package cn.variZoo.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    public static Message instance;
    private String breedActionbar;

    public Message() {
        instance = this;
    }


    public static void sendMsg(CommandSender sender, String message) {
        if (sender instanceof Player p) {
            p.sendMessage(message);
        } else {
            String[] parts = message.split("\\n");
            for (String part : parts) {
                XLogger.info(part);
            }
        }
    }

    public static void showHelp(CommandSender sender) {
        String message = "VariZoo help\n" +
                "/varizoo reload 重启插件";
        sendMsg(sender, message);

    }


}
