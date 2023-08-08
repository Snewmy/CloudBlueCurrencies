package me.sam;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GUIItem {

    Material material;
    boolean glow;
    String displayName;
    List<String> lore = new ArrayList<>();
    List<String> commands = new ArrayList<>();
    List<Integer> slots = new ArrayList<>();
    int cost;
    int amount;

    boolean closeButton;

    public GUIItem(List<Integer> slots, Material material, boolean glow, String displayName, List<String> lore, int cost, boolean closeButton, int amount, List<String> commands) {
        this.slots = slots;
        this.material = material;
        this.glow = glow;
        this.displayName = displayName;
        this.lore = lore;
        this.cost = cost;
        this.closeButton = closeButton;
        this.amount = amount;
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isGlow() {
        return glow;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isCloseButton() {
        return closeButton;
    }

    public int getCost() {
        return cost;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

}
