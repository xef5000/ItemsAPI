package com.github.xef5000.itemsapi.api.material;

import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.nova.api.Nova;
import xyz.xenondevs.nova.api.item.NovaItem;

public class NovaMaterialHandler implements MaterialHandler {
    @Override
    public String getPrefix() {
        return "nova";
    }

    @Override
    public ItemStack parse(String materialString) {
        Nova nova = Nova.getNova();
        NovaItem novaItem = nova.getItemRegistry().get(materialString);
        return novaItem.createItemStack();
    }
}
