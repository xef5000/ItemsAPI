package com.github.xef5000.itemsapi.api.material;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.inventory.ItemStack;

public class MythicMobsMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "mythicmobs";
    }

    @Override
    public ItemStack parse(String materialString) {
        try (MythicBukkit instance = MythicBukkit.inst()) {
            if (instance == null) {
                throw new RuntimeException("Mythic Bukkit instance is null (try adding it as a dependency)");
            }
            return instance
                    .getItemManager()
                    .getItemStack(materialString);
        }

    }
}
