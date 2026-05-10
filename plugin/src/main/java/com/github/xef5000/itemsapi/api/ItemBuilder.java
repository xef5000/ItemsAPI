package com.github.xef5000.itemsapi.api;

import com.github.xef5000.itemsapi.api.adapter.ItemMetaAdapter;
import com.github.xef5000.itemsapi.api.material.*;
import com.github.xef5000.itemsapi.api.utils.ColorHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
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
    public final String material;
    public final int amount;
    public final String name;
    public final Integer customModelData;
    public final List<String> lore;
    public final int durability;
    public final boolean unbreakable;
    public final Map<String, Object> components;
    public final Map<Enchantment, Integer> enchantments;
    public final List<String> flags;

    public ItemBuilder(String material, int amount, String name, Integer customModelData, List<String> lore, int durability, boolean unbreakable, Map<String, Object> components, Map<Enchantment, Integer> enchantments, List<String> flags) {
        this.item = parseItemFromName(material);
        this.meta = item.getItemMeta();

        this.material = material;
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

    public static ItemStack parseItemFromName(String materialString) {
        ItemStack item = null;
        for (MaterialHandler handler : materialHandlers) {
            if (handler.canHandle(materialString)) {
                String data = handler.getPrefix().isEmpty() ?
                        materialString :
                        materialString.substring(handler.getPrefix().length() + 1);
                if (data.isEmpty())
                    throw new IllegalArgumentException("Material string cannot be null or empty.");

                item = handler.parse(materialString);
                break;
            }
        }

        if (item == null)
            throw new IllegalArgumentException("Could not handle material string: " + materialString);

        return item;
    }
}
