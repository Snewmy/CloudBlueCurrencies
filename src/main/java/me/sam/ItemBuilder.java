package me.sam;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    public String name;
    public ItemStack item;
    public Material m;
    public int amount;
    public ArrayList<String> lore = new ArrayList<String>();

    public ItemBuilder(Material m, int amount, String... lore) {
        this.item = new ItemStack(m, amount);
        ItemMeta im = this.item.getItemMeta();
        im.setLore(Arrays.asList(lore));

        this.item.setItemMeta(im);
        this.amount = amount;
        this.m = m;
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public Material getMaterial() {
        return this.m;
    }

    public int getAmount() {
        return this.amount;
    }

    public String getDisplayName() {
        return this.name;
    }

    public ArrayList<String> getLore() {
        return this.lore;
    }

    public ItemBuilder addPersistentDataString(String s){
        ItemMeta im = this.item.getItemMeta();
        im.getPersistentDataContainer().set(CloudyCurrencies.instance.nameKey, PersistentDataType.STRING, s);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addPersistentDataString(NamespacedKey nameKey, String s){
        ItemMeta im = this.item.getItemMeta();
        im.getPersistentDataContainer().set(nameKey, PersistentDataType.STRING, s);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addPersistentDataDouble(double d){
        ItemMeta im = this.item.getItemMeta();
        im.getPersistentDataContainer().set(CloudyCurrencies.instance.nameKey, PersistentDataType.DOUBLE, d);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag){
        ItemMeta im = this.item.getItemMeta();
        im.addItemFlags(flag);
        this.item.setItemMeta(im);
        return this;
    }

    public void setGlowing() {
        addEnchants(Enchantment.DIG_SPEED, 1);
        addItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore.addAll(lore);
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name);
        this.item.setItemMeta(im);
        this.name = name;
        return this;
    }

    public ItemBuilder addCustomModelData(int data) {
        ItemMeta im = this.item.getItemMeta();
        im.setCustomModelData(data);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.addAttributeModifier(attribute, attributeModifier);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    public ItemBuilder addEnchants(Enchantment ench, int level) {
        this.item.addUnsafeEnchantment(ench, level);
        return this;
    }
}
