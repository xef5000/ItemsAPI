package com.github.xef5000.itemsapi;

import com.github.xef5000.itemsapi.api.ItemBuilder;
import com.github.xef5000.api.material.*;
import com.github.xef5000.itemsapi.api.material.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

        // Minecraft mechanic handlers
        registerHandler(new Base64MaterialHandler());

        registerHandler(new MinecraftMaterialHandler()); // Fallback
    }

    public static void registerHandler(MaterialHandler handler) {
        // Prepend to ensure custom handlers are checked before the default
        materialHandlers.add(handler);
    }

    public static ItemStack fromConfiguration(ConfigurationSection section) {
        if (section == null) {
            throw new IllegalArgumentException("ConfigurationSection cannot be null.");
        }

        String materialString = section.getString("material");
        if (materialString == null || materialString.isEmpty()) {
            throw new IllegalArgumentException("ConfigurationSection must contain a 'material' field.");
        }

        // 1. Find the right handler and get the base ItemStack
        ItemStack item = null;
        for (MaterialHandler handler : materialHandlers) {
            if (handler.canHandle(materialString)) {
                item = handler.parse(section);
                break;
            }
        }

        if (item == null) {
            throw new IllegalArgumentException("Could not handle material string: " + materialString);
        }

        // 2. Apply all other optional properties
        ItemBuilder.start(item, section)
                .withAmount()
                .withName()
                .withLore()
                .withCustomModelData() // This method will use the VersionFactory
                .withEnchants()
                .withItemFlags()
                .withAttributes()
                .withUnbreakable()
                .withComponents() // Override legacy components
                .apply(); // Apply the final ItemMeta

        return item;
    }

}
