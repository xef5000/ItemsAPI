package com.github.xef5000.api.material;

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
    boolean canHandle(String materialString);

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
        String data = section.getString("material").substring(getPrefix().length() + 1);
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Material string cannot be null or empty.");
        }
        if (!canHandle(data)) {
            throw new IllegalArgumentException("This handler cannot handle the material string: " + data);
        }
        return parse(data);
    }
}
