package com.github.xef5000.api.material;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class NexoMaterialHandler implements MaterialHandler {
    @Override
    public String getPrefix() {
        return "nexo";
    }

    @Override
    public boolean canHandle(String materialString) {
        return materialString.toLowerCase().startsWith(getPrefix() + ":");
    }

    @Override
    public ItemStack parse(String materialString) {
        ItemBuilder itemBuilder = NexoItems.itemFromId(materialString);
        if (itemBuilder != null) {
            return itemBuilder.build();
        } else {
            throw new IllegalArgumentException("Invalid Nexo material specified: '" + materialString + "'");
        }
    }
}
