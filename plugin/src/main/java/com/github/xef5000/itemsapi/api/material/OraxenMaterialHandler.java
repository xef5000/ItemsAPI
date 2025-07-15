package com.github.xef5000.itemsapi.api.material;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class OraxenMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "oraxen";
    }

    @Override
    public ItemStack parse(String materialString) {
        ItemBuilder itemBuilder = OraxenItems.getItemById(materialString);
        if (itemBuilder != null) {
            return itemBuilder.build();
        } else {
            throw new IllegalArgumentException("Invalid Oraxen material specified: '" + materialString + "'");
        }
    }
}
