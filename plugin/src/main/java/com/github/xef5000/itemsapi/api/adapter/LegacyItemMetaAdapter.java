package com.github.xef5000.itemsapi.api.adapter;

import org.bukkit.inventory.meta.ItemMeta;

public class LegacyItemMetaAdapter implements ItemMetaAdapter {
    @Override
    public void applyCustomModelData(ItemMeta meta, int data) {
        // Legacy versions (pre-1.14) do not support custom model data
    }

    @Override
    public boolean hasCustomModelData(ItemMeta meta) {
        return false;
    }
}
