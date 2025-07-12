package com.github.xef5000.api.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MinecraftMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public boolean canHandle(String materialString) {
        return true;
    }

    @Override
    public ItemStack parse(String materialString) {
        Material material = Material.matchMaterial(materialString);
        if (material == null) {
            throw new IllegalArgumentException("Invalid Minecraft material specified: '" + materialString + "'");
        }
        return new ItemStack(material);
    }
}
