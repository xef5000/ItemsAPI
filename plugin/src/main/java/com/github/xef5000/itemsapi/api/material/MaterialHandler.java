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
}
