package com.github.xef5000.itemsapi.api.material;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ExecutableItemsMaterialHandler implements MaterialHandler{
    @Override
    public String getPrefix() {
        return "executableitems";
    }

    @Override
    public ItemStack parse(String materialString) {
        ItemStack item = null;
        Optional<ExecutableItemInterface> eiOpt = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(materialString);
        if (eiOpt.isPresent())
            item = eiOpt.get().buildItem(1, Optional.empty());
        return item;
    }
}
