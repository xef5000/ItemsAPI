package com.github.xef5000.itemsapi.api.material;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface MaterialHandler {
    /**
     * The prefix this handler looks for (e.g., "base64", "itemsadder").
     * Can be null or empty for a default/fallback handler.
     */
    String getPrefix();

    /**
     * Checks if this handler can process the given material string.
     */
    default boolean canHandle(String materialString) {
        return materialString.toLowerCase().startsWith(getPrefix() + ":");
    };

    /**
     * Receives the data string and returns an ItemStack.
     * Will receive "custom_data", not "prefix:custom_data".
     * */
    ItemStack parse(String materialString);

    /**
     * Parses a ConfigurationSection to create an ItemStack.
     * This method expects the section to have a "material" field
     */
    default ItemStack parse(ConfigurationSection section) {
        String data = getPrefix().isEmpty() ?
                section.getString("material") :
                section.getString("material").substring(getPrefix().length() + 1);
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Material string cannot be null or empty.");
        }
        return parse(data);
    }
}
