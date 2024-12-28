package cn.variZoo;

import cn.variZoo.utils.Message;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Command implements CommandExecutor, TabCompleter {
    private final List<String> subcommands;
    private final Main plugin;

    public Command(Main main) {
        this.plugin = main;
        subcommands = new ArrayList<>(List.of("reload", "help"));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0 || !subcommands.contains(args[0].toLowerCase())) {
            Message.showHelp(sender);
            return true;
        }

        if (!sender.hasPermission("varizoo." + args[0])) {
            Message.send(sender, "你没有权限执行此命令!");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                long startTime = System.currentTimeMillis();
                Message.send(sender, "插件重启中...");
                plugin.reload();
                long elapsedTime = System.currentTimeMillis() - startTime;
                Message.send(sender, "VariZoo重启完成，耗时 " + elapsedTime + " ms");
                break;
            case "help":
                Message.showHelp(sender);
                break;
        }
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> ret = new ArrayList<>();
        if (args.length == 1) {
            for (String subcmd : subcommands) {
                if (!sender.hasPermission("varizoo." + subcmd)) continue;
                ret.add(subcmd);
            }
            return StringUtil.copyPartialMatches(args[0].toLowerCase(), ret, new ArrayList<>());
        }

        return Collections.emptyList();
    }
}
