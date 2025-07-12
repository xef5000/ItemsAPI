package com.github.xef5000.api.material;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderMaterialHandler implements MaterialHandler {

    @Override
    public String getPrefix() {
        return "itemsadder";
    }

    @Override
    public ItemStack parse(String materialString) {
        CustomStack customStack = CustomStack.getInstance(materialString);
        if (customStack != null) {
            return customStack.getItemStack();
        } else {
            throw new IllegalArgumentException("Invalid ItemsAdder material specified: '" + materialString + "'");
        }
    }
}
