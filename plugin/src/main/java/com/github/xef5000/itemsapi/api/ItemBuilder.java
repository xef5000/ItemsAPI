package com.github.xef5000.itemsapi.api;

import com.github.xef5000.itemsapi.api.adapter.ItemMetaAdapter;
import com.github.xef5000.itemsapi.api.utils.ColorHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    // Adapters
    private final ItemMetaAdapter metaAdapter = VersionFactory.getItemMetaAdapter(); // Get version adapter
    private final NMSAdapter nmsAdapter = VersionFactory.getNmsAdapter(); // Get NMS adapter

    // Values
    private final int amount;
    private final String name;
    private final Integer customModelData;
    private final List<String> lore;
    private final int durability;
    private final boolean unbreakable;
    private final Map<String, Object> components;
    private final Map<Enchantment, Integer> enchantments;
    private final List<String> flags;

    public ItemBuilder(ItemStack item, int amount, String name, int customModelData, List<String> lore, int durability, boolean unbreakable, Map<String, Object> components, Map<Enchantment, Integer> enchantments, List<String> flags) {
        this.item = item;
        this.meta = item.getItemMeta();

        this.amount = amount;
        this.name = name;
        this.customModelData = customModelData;
        this.lore = lore;
        this.durability = durability;
        this.unbreakable = unbreakable;
        this.components = components;
        this.enchantments = enchantments;
        this.flags = flags;
    }

    public ItemBuilder withAmount() {
        item.setAmount(this.amount);
        return this;
    }

    public ItemBuilder withComponents() {
        if (this.components != null)
            nmsAdapter.applyComponents(meta, this.components);

        return this;
    }

    public ItemBuilder withName() {
        if (this.name != null)
            meta.setDisplayName(ColorHelper.translate(this.name));

        return this;
    }

    public ItemBuilder withCustomModelData() {
        if (this.customModelData != null)
            metaAdapter.applyCustomModelData(meta, this.customModelData);

        return this;
    }

    public ItemBuilder withLore() {
        if (lore != null)
            meta.setLore(ColorHelper.translate(this.lore));

        return this;
    }

    public ItemBuilder withDurability() {
        if (meta instanceof Damageable damageable)
            damageable.setDamage(durability); // Default is zero

        return this;
    }

    public ItemBuilder withEnchants() {
        if (this.enchantments != null) {
            enchantments.forEach((enchantment, level) -> {
                if (enchantment != null) {
                    meta.addEnchant(enchantment, level, true);
                } else {
                    System.err.println("Invalid enchantment in config: " + enchantment);
                }
            });
        }
        return this;
    }

    public ItemBuilder withItemFlags() {
        if (this.flags != null) {
            this.flags.forEach(flag -> {
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
        if (this.unbreakable)
            meta.setUnbreakable(true);

        return this;
    }

    // ... method for each property (lore, enchants, flags, etc.)

    public ItemStack apply() {
        item.setItemMeta(meta);
        return item;
    }
}
