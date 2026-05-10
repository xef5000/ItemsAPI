package com.github.xef5000.itemsapi.api;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public interface NMSAdapter {
    /**
     * Applies modern item components from a configuration section to an ItemMeta.
     * On older versions, this might do nothing or log a warning.
     * On modern versions, this will parse and apply the component data.
     *
     * @param stack             The ItemStack to modify.
     * @param components The components map containing the component data.
     */
    void applyComponents(ItemMeta stack, Map<String, Object> components);
}
