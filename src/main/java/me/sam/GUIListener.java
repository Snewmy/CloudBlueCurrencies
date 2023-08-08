package me.sam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Utils.chat(CloudyCurrencies.instance.config.getString("guititle")))) {
            event.setCancelled(true);
            Debugger.log("GUI Click event");
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            ItemStack itemStack = event.getCurrentItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.getPersistentDataContainer().has(CloudyCurrencies.instance.nameKey)) {
                BankAccount bankAccount = CloudyCurrencies.accounts.get(player.getUniqueId());
                GUIItem guiItem = CloudyCurrencies.guiItems.get(itemMeta.getPersistentDataContainer().get(CloudyCurrencies.instance.nameKey, PersistentDataType.STRING));
                if (guiItem.isCloseButton()) {
                    player.closeInventory();
                    return;
                }
                if (guiItem.getCost() > 0) {
                    if (bankAccount.getCoins() < guiItem.getCost()) {
                        player.sendMessage(Utils.chat(Locale.instance.get("notenoughcoins")));
                        return;
                    }
                    new Confirmer(player, "Please confirm your purchase!", Material.BOOK, true, new BiConsumer<Player, Boolean>() {
                        @Override
                        public void accept(Player player, Boolean confirmed) {
                            if (confirmed) {
                                player.sendMessage(Utils.chat(Locale.instance.get("purchasesuccess").replace("%itemname%", guiItem.getDisplayName()).replace("%coins%", guiItem.getCost() + "")));
                                for (String command : guiItem.getCommands()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                                    Debugger.log("Dispatching command '" + command.replace("%player%", player.getName()) + "'");
                                }
                                bankAccount.takeCoins(guiItem.getCost());
                                Debugger.log("Player " + player.getName() + " purchased a " + guiItem.getDisplayName() + " for " + guiItem.getCost() + " coins");
                            }
                            CloudyCurrencies.instance.createGUI(player);
                        }
                    }, new Consumer<Player>() {
                        @Override
                        public void accept(Player player) {
                            CloudyCurrencies.instance.createGUI(player);
                        }
                    }, ChatColor.RED + "Confirmation", CloudyCurrencies.instance);
                }
            }
        }
    }
}
