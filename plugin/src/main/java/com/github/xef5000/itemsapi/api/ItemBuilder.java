package com.github.xef5000.itemsapi.api;

import com.github.xef5000.itemsapi.api.adapter.ItemMetaAdapter;
import com.github.xef5000.itemsapi.api.utils.ColorHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;
    private final ConfigurationSection section;
    private final ItemMetaAdapter metaAdapter = VersionFactory.getItemMetaAdapter(); // Get version adapter
    private final NMSAdapter nmsAdapter = VersionFactory.getNmsAdapter(); // Get NMS adapter

    private ItemBuilder(ItemStack item, ConfigurationSection section) {
        this.item = item;
        this.meta = item.getItemMeta();
        this.section = section;
    }

    public static ItemBuilder start(ItemStack item, ConfigurationSection section) {
        return new ItemBuilder(item, section);
    }

    public ItemBuilder withAmount() {
        if (section.contains("amount")) {
            item.setAmount(section.getInt("amount"));
        }
        return this;
    }

    public ItemBuilder withComponents() {
        if (section.contains("components")) {
            // Use the adapter here!
            nmsAdapter.applyComponents(meta, section.getConfigurationSection("components"));
        }
        return this;
    }

    public ItemBuilder withName() {
        if (section.contains("name")) {
            meta.setDisplayName(ColorHelper.translate(section.getString("name")));
        }
        return this;
    }

    public ItemBuilder withCustomModelData() {
        if (section.contains("custom-model-data")) {
            // Use the adapter here!
            metaAdapter.applyCustomModelData(meta, section.getInt("custom-model-data"));
        }
        return this;
    }

    public ItemBuilder withLore() {
        if (section.contains("lore")) {
            meta.setLore(ColorHelper.translate(section.getStringList("lore")));
        }
        return this;
    }

    public ItemBuilder withEnchants() {
        if (section.contains("enchants")) {
            section.getConfigurationSection("enchants").getKeys(false).forEach(key -> {
                String enchantment = key.toUpperCase();
                int level = section.getInt("enchants." + key);
                meta.addEnchant(Objects.requireNonNull(Enchantment.getByName(enchantment)), level, true);
            });
        }
        return this;
    }

    public ItemBuilder withItemFlags() {
        if (section.contains("flags")) {
            section.getStringList("flags").forEach(flag -> {
                try {
                    meta.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid flag names gracefully
                    System.err.println("Invalid item flag: " + flag);
                }
            });
        }
        return this;
    }

    public ItemBuilder withAttributes() {
        // Placeholder for future attribute handling
        // This could be expanded to handle attributes like attack damage, speed, etc.
        return this;
    }

    public ItemBuilder withUnbreakable() {
        if (section.getBoolean("unbreakable", false)) {
            meta.setUnbreakable(true);
        }
        return this;
    }

    // ... method for each property (lore, enchants, flags, etc.)

    public void apply() {
        item.setItemMeta(meta);
    }
}
