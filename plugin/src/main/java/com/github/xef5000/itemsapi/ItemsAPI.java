package com.github.xef5000.itemsapi;

import com.github.xef5000.itemsapi.api.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemsAPI {

    public static ItemBuilder createBuilder(String materialString, int amount, String name, Integer customModelData, List<String> lore, int durability, boolean unbreakable, Map<String, Object> components, Map<Enchantment, Integer> enchantments, List<String> flags) {
        // Apply all other optional properties
        return new ItemBuilder(
                materialString,
                amount,
                name,
                customModelData,
                lore,
                durability,
                unbreakable,
                components,
                enchantments,
                flags
        )
                .withAmount()
                .withName()
                .withLore()
                .withDurability()
                .withCustomModelData()
                .withEnchants()
                .withItemFlags()
                .withAttributes()
                .withUnbreakable()
                .withComponents(); // Override legacy components
    }

    public static ItemBuilder builderFromConfiguration(ConfigurationSection section) {
        if (section == null) {
            throw new IllegalArgumentException("ConfigurationSection cannot be null.");
        }

        String materialString = section.getString("material");
        return createBuilder(
                materialString,
                section.getInt("amount", 1),
                section.getString("name", null),
                section.contains("custom-model-data") ? section.getInt("custom-model-data", 0) : null,
                section.getStringList("lore"),
                section.getInt("durability", 0),
                section.getBoolean("unbreakable", false),
                section.contains("components") ? section.getConfigurationSection("components").getValues(false) : null,
                section.contains("enchants") ? Objects.requireNonNull(section.getConfigurationSection("enchants")).getKeys(false).stream().collect(java.util.stream.Collectors.toMap(
                        key -> Enchantment.getByName(key.toUpperCase()),
                        key -> section.getInt("enchants." + key)
                )) : null,
                section.getStringList("flags")
        );
    }

    public static ItemStack fromConfiguration(ConfigurationSection section) {
        return builderFromConfiguration(section).apply();
    }

}
