package me.sam;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoinsTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("coins")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("CloudBlueCurrencies.admin")) {
                    return completions;
                }
                if (args.length == 1) {
                    commands.add("give");
                    commands.add("take");
                    commands.add("reload");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        commands.add(p.getName());
                    }
                    StringUtil.copyPartialMatches(args[0], commands, completions);
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            commands.add(p.getName());
                        }
                    }
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
