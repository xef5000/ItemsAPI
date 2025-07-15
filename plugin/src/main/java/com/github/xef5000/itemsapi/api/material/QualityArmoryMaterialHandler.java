package com.github.xef5000.itemsapi.api.material;

import me.zombie_striker.qg.api.QualityArmory;
import org.bukkit.inventory.ItemStack;

public class QualityArmoryMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "qualityarmory";
    }

    @Override
    public ItemStack parse(String materialString) {
        return QualityArmory.getCustomItemAsItemStack(materialString);
    }
}
