package com.github.xef5000.itemsapi.api.material;

import com.willfp.eco.core.items.Items;
import org.bukkit.inventory.ItemStack;

public class EcoMaterialHandler implements MaterialHandler {
    @Override
    public String getPrefix() {
        return "eco";
    }

    @Override
    public ItemStack parse(String materialString) {
        return Items.lookup(materialString).getItem();
    }
}
