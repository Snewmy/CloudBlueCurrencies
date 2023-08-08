package me.sam;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CloudyCurrencies extends JavaPlugin {

    public static CloudyCurrencies instance;
    public static HashMap<UUID, BankAccount> accounts = new HashMap<UUID, BankAccount>();
    public File dataFile = new File(getDataFolder(), "data.yml");
    public FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
    public File messagesFile;
    public FileConfiguration messages;
    public FileConfiguration config;
    public static HashMap<String, GUIItem> guiItems = new HashMap<>();
    public NamespacedKey nameKey = new NamespacedKey(this, "coinshop");

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();
        if (!data.isConfigurationSection("data")) {
            data.createSection("data");
            try {
                data.save(dataFile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveResource("messages.yml", false);
        messagesFile = new File(getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        new Locale();
        loadData();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new GUIListener(), this);
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("coins").setTabCompleter(new CoinsTabCompleter());
        getCommand("coinshop").setExecutor(new CoinShopCommand());
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public void saveData() {
        for (Map.Entry<UUID, BankAccount> entry : accounts.entrySet()) {
            String uuid = entry.getKey().toString();
            BankAccount bankAccount = entry.getValue();
            data.set("data." + uuid + ".coins", bankAccount.getCoins());
            Debugger.log("Saving " + uuid + " with " + bankAccount.getCoins() + " coin(s) to data.yml");
        }
        try {
            data.save(dataFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void loadData() {
        for (String uuid : data.getConfigurationSection("data").getKeys(false)) {
            int coins = data.getInt("data." + uuid + ".coins");
            BankAccount bankAccount = new BankAccount(coins);
            accounts.put(UUID.fromString(uuid), bankAccount);
            Debugger.log("Loaded " + coins + " coin(s) from " + uuid);
        }
    }

    public void savePlayer(UUID uuid, BankAccount bankAccount) {
        data.set("data." + uuid.toString() + ".coins", bankAccount.getCoins());
        Debugger.log("Saving " + uuid + " with " + bankAccount.getCoins() + " coin(s) to data.yml");
        try {
            data.save(dataFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void createGUI(Player player) {
        BankAccount bankAccount = accounts.get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, config.getInt("guisize"), Utils.chat(config.getString("guititle")));
        ConfigurationSection configSec = config.getConfigurationSection("items");
        for (String itemNumber : configSec.getKeys(false)) {
            List<Integer> slots = new ArrayList<>();
            slots.addAll(configSec.getIntegerList(itemNumber + ".slots"));
            Material material = Material.valueOf(configSec.getString(itemNumber + ".material"));
            String displayName = Utils.chat(configSec.getString(itemNumber + ".name"));
            List<String> lore = new ArrayList<>();
            for (String loreLine : configSec.getStringList(itemNumber + ".lore")) {
                lore.add(Utils.chat(loreLine));
            }
            boolean glow = configSec.getBoolean(itemNumber + ".glow", false);
            int cost = configSec.getInt(itemNumber + ".cost");
            boolean isCloseButton = configSec.getBoolean(itemNumber + ".closebutton", false);
            int amount = configSec.getInt(itemNumber + ".amount");
            List<String> commands = new ArrayList<>();
            commands.addAll(configSec.getStringList(itemNumber + ".commands"));
            GUIItem guiItem = new GUIItem(slots, material, glow, displayName, lore, cost, isCloseButton, amount, commands);
            guiItems.put(itemNumber, guiItem);
            Debugger.log("Loaded gui item. material: " + material + " glow:" + glow + " displayname:" + displayName + " cost:" + cost + " closebutton:" + isCloseButton + " amount:" + amount);
        }

        for (Map.Entry<String, GUIItem> entry : guiItems.entrySet()) {
            String itemNumber = entry.getKey();
            GUIItem guiItem = entry.getValue();
            ItemBuilder itemBuilder = new ItemBuilder(guiItem.getMaterial(), guiItem.getAmount())
                    .setDisplayName(guiItem.getDisplayName().replace("%coins%", bankAccount.getCoins() + ""))
                    .setLore(guiItem.getLore())
                    .addPersistentDataString(itemNumber);
            if (guiItem.isGlow())
                itemBuilder.setGlowing();
            ItemStack itemStack = itemBuilder.toItemStack();
            for (int slot : guiItem.getSlots()) {
                inventory.setItem(slot, itemStack);
            }
            Debugger.log("Added item " + guiItem.getMaterial().toString() + " to GUI");
        }
        player.openInventory(inventory);
    }
}