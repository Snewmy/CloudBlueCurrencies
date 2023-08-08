package me.sam;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CoinsCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("coins")) {
                if (args.length == 0) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        BankAccount bankAccount = CloudyCurrencies.accounts.get(player.getUniqueId());
                        player.sendMessage(Utils.chat(Locale.instance.get("coinsbalance").replace("%amount%", bankAccount.getCoins() + "")));
                        return false;
                    }
                }
                if (!sender.hasPermission("CloudBlueCurrencies.admin")) {
                    sender.sendMessage(Utils.chat(Locale.instance.get("nopermission")));
                    return false;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        long startTime = System.currentTimeMillis();
                        CloudyCurrencies.instance.saveData();
                        CloudyCurrencies.accounts.clear();
                        CloudyCurrencies.instance.loadData();
                        CloudyCurrencies.instance.messages = YamlConfiguration.loadConfiguration(CloudyCurrencies.instance.messagesFile);
                        CloudyCurrencies.guiItems.clear();
                        CloudyCurrencies.instance.reloadConfig();
                        CloudyCurrencies.instance.config = CloudyCurrencies.instance.getConfig();
                        long timeTaken = System.currentTimeMillis() - startTime;
                        sender.sendMessage(Utils.chat(Locale.instance.get("reloadmessage").replace("%time%", timeTaken + "")));
                        Debugger.log("Successfully reloaded the plugin.");
                        return false;
                    } else if (Bukkit.getPlayer(args[0]) != null) {
                        Player receiver = Bukkit.getPlayer(args[0]);
                        BankAccount bankAccount = CloudyCurrencies.accounts.get(receiver.getUniqueId());
                        sender.sendMessage(Utils.chat(Locale.instance.get("coinsbalanceother").replace("%name%", receiver.getName()).replace("%amount%", bankAccount.getCoins() + "")));
                        return false;
                    } else {
                        for (String string : Locale.instance.getList("commandlist")) {
                            sender.sendMessage(Utils.chat(string));
                        }
                    }
                    return false;
                } else if (args.length >= 3) {
                    if (args[0].equalsIgnoreCase("give")) {
                        String playerName = args[1];
                        if (Bukkit.getPlayer(playerName) == null) {
                            sender.sendMessage(Utils.chat(Locale.instance.get("invalidplayer")));
                            return false;
                        }
                        try {
                            int amount = Integer.parseInt(args[2]);
                            Player receiver = Bukkit.getPlayer(playerName);
                            BankAccount bankAccount = CloudyCurrencies.accounts.get(receiver.getUniqueId());
                            sender.sendMessage(Utils.chat(Locale.instance.get("givemessage").replace("%name%", receiver.getName()).replace("%amount%", amount + "")));
                            receiver.sendMessage(Utils.chat(Locale.instance.get("givereceivedmessage").replace("%amount%", amount + "")));
                            bankAccount.addCoins(amount);
                            CloudyCurrencies.instance.savePlayer(receiver.getUniqueId(), bankAccount);
                            Debugger.log("Gave player " + receiver.getName() + " " + amount + " coin(s)");
                        } catch (NumberFormatException e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    } else if (args[0].equalsIgnoreCase("take")) {
                        String playerName = args[1];
                        if (Bukkit.getPlayer(playerName) == null) {
                            sender.sendMessage(Utils.chat(Locale.instance.get("invalidplayer")));
                            return false;
                        }
                        try {
                            int amount = Integer.parseInt(args[2]);
                            Player receiver = Bukkit.getPlayer(playerName);
                            BankAccount bankAccount = CloudyCurrencies.accounts.get(receiver.getUniqueId());
                            if (amount > bankAccount.getCoins()) {
                                sender.sendMessage(Utils.chat(Locale.instance.get("takingtoomany").replace("%amount%", bankAccount.getCoins() + "")));
                                return false;
                            }
                            sender.sendMessage(Utils.chat(Locale.instance.get("takemessage").replace("%name%", receiver.getName()).replace("%amount%", amount + "")));
                            receiver.sendMessage(Utils.chat(Locale.instance.get("takereceivedmessage").replace("%amount%", amount + "")));
                            bankAccount.takeCoins(amount);
                            CloudyCurrencies.instance.savePlayer(receiver.getUniqueId(), bankAccount);
                            Debugger.log("Took " + amount + " coin(s) from " + receiver.getName());
                        } catch (NumberFormatException e1) {
                            e1.printStackTrace();
                        }
                        return false;
                    }
                } else {
                    for (String string : Locale.instance.getList("commandlist")) {
                        sender.sendMessage(Utils.chat(string));
                    }
                }
            }
        return false;
    }
}
