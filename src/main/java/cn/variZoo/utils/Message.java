package cn.variZoo.utils;

import cn.variZoo.Configuration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    public static Message instance;
    private String breedActionbar;

    public Message() {
        instance = this;
        load();
    }

    public static void load() {
        instance.breedActionbar = Configuration.Breed.inheritance.actionbar
                .replace("{scale}", "<scale>")
                .replace("{baby}", "<translate:<baby>>")
                .replace("{player}", "<player>");

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


    public static void sendActionbar(String message, Player player) {
        player.sendActionBar(MiniMessage.miniMessage().deserialize(message));
    }

    public static void sendBreedActionbar(Double scale, String baby, Player player) {
        Component actionbar = MiniMessage.miniMessage().deserialize(instance.breedActionbar,
                Placeholder.parsed("scale", String.format("%.2f", scale)),
                Placeholder.parsed("baby", baby),
                Placeholder.parsed("player", player.getName())
        );
        player.sendActionBar(actionbar);
    }

}
