package com.github.xef5000.api;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface NMSAdapter {
    /**
     * Applies modern item components from a configuration section to an ItemMeta.
     * On older versions, this might do nothing or log a warning.
     * On modern versions, this will parse and apply the component data.
     *
     * @param stack             The ItemStack to modify.
     * @param componentSection The ConfigurationSection containing the component data.
     */
    void applyComponents(ItemStack stack, ConfigurationSection componentSection);
}
