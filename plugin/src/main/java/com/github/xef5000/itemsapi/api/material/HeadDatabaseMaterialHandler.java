package com.github.xef5000.itemsapi.api.material;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.inventory.ItemStack;

public class HeadDatabaseMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "hdb";
    }

    @Override
    public ItemStack parse(String materialString) {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        if (api.isHead(materialString)) {
            return api.getItemHead(materialString);
        } else {
            throw new IllegalArgumentException("Invalid HeadDatabase material specified: '" + materialString + "'");
        }
    }
}
