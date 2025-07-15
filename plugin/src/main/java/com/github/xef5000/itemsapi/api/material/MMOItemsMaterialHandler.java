package com.github.xef5000.itemsapi.api.material;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;

public class MMOItemsMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "mmoitems";
    }

    @Override
    public ItemStack parse(String materialString) {
        String type = materialString.split(":")[0];
        String id = materialString.split(":")[1];
        MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), id);
        if (mmoitem != null) {
            return mmoitem.newBuilder().build();
        } else {
            throw new IllegalArgumentException("Invalid MMOItems material specified: '" + materialString + "'");
        }
    }
}
