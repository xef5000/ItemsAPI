package com.github.xef5000.api.adapter;

import org.bukkit.inventory.meta.ItemMeta;

public class ModernItemMetaAdapter implements ItemMetaAdapter {
    @Override
    public void applyCustomModelData(ItemMeta meta, int data) {
        meta.setCustomModelData(data);
    }

    @Override
    public boolean hasCustomModelData(ItemMeta meta) {
        return meta.hasCustomModelData();
    }
}
