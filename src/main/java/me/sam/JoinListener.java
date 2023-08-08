package me.sam;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!CloudyCurrencies.accounts.containsKey(event.getPlayer().getUniqueId())) {
            BankAccount bankAccount = new BankAccount();
            CloudyCurrencies.accounts.put(event.getPlayer().getUniqueId(), bankAccount);
            Debugger.log("Player " + event.getPlayer().getName() + " does not have a coin account. Creating..");
        }
    }
}
