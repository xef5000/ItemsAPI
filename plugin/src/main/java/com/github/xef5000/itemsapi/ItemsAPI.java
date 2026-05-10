package com.github.xef5000.itemsapi;

import com.github.xef5000.itemsapi.api.ItemBuilder;
import com.github.xef5000.itemsapi.api.material.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemsAPI {

    // A list of all registered material handlers
    private static final List<MaterialHandler> materialHandlers = new ArrayList<>();

    // Static initializer to register your handlers
    static {
        // Custom plugins material handlers
        registerHandler(new NexoMaterialHandler());
        registerHandler(new ItemsAdderMaterialHandler());
        registerHandler(new OraxenMaterialHandler());
        registerHandler(new MMOItemsMaterialHandler());
        registerHandler(new HeadDatabaseMaterialHandler());
        registerHandler(new MythicMobsMaterialHandler());
        registerHandler(new ExecutableItemsMaterialHandler());
        registerHandler(new QualityArmoryMaterialHandler());
        registerHandler(new NovaMaterialHandler());
        registerHandler(new EcoMaterialHandler());
        registerHandler(new WildToolsMaterialHandler());

        // Minecraft mechanic handlers
        registerHandler(new Base64MaterialHandler());

        registerHandler(new MinecraftMaterialHandler()); // Fallback
    }

    public static void registerHandler(MaterialHandler handler) {
        // Prepend to ensure custom handlers are checked before the default
        materialHandlers.add(handler);
    }

    public static ItemBuilder createBuilder(String materialString, int amount, String name, int customModelData, List<String> lore, int durability, boolean unbreakable, Map<String, Object> components, Map<Enchantment, Integer> enchantments, List<String> flags) {
        ItemStack item = null;
        for (MaterialHandler handler : materialHandlers) {
            if (handler.canHandle(materialString)) {
                String data = handler.getPrefix().isEmpty() ?
                        materialString :
                        materialString.substring(handler.getPrefix().length() + 1);
                if (data == null || data.isEmpty()) {
                    throw new IllegalArgumentException("Material string cannot be null or empty.");
                }
                item = handler.parse(materialString);
                break;
            }
        }

        if (item == null) {
            throw new IllegalArgumentException("Could not handle material string: " + materialString);
        }

        // Apply all other optional properties
        return new ItemBuilder(item,
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
