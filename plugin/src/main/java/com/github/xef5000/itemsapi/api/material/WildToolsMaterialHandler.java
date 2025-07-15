package com.github.xef5000.itemsapi.api.material;

import com.bgsoftware.wildtools.api.WildToolsAPI;
import org.bukkit.inventory.ItemStack;

public class WildToolsMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "wildtools";
    }

    @Override
    public ItemStack parse(String materialString) {
        return WildToolsAPI.getTool(materialString).getItemStack();
    }
}
