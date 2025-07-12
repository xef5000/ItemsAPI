package com.github.xef5000.api.adapter;

import org.bukkit.inventory.meta.ItemMeta;

public interface ItemMetaAdapter {
    void applyCustomModelData(ItemMeta meta, int data);
    boolean hasCustomModelData(ItemMeta meta);
}
