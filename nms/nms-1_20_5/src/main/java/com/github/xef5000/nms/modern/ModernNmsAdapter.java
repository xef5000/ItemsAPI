package com.github.xef5000.nms.modern;

import com.github.xef5000.api.NMSAdapter;
import com.github.xef5000.api.util.ConfigToJsonConverter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponentPatch;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

import java.util.Map;
import java.util.Optional;

public class ModernNmsAdapter implements NMSAdapter {
    private static final Gson GSON = new Gson();

    @Override
    public void applyComponents(org.bukkit.inventory.ItemStack meta, ConfigurationSection componentSection) {
        // 1. Guard Clause: Do nothing if there's no component section.
        if (componentSection == null) {
            return;
        }

        if (!(meta instanceof CraftItemStack craftMeta)) {
            Bukkit.getLogger().warning("[ItemsAPI] Could not apply components: ItemMeta is not an instance of CraftMetaItem.");
            return;
        }

        try {
            // 2. Convert the Bukkit ConfigurationSection into a format that Mojang's serializer understands.
            // ConfigSection -> Map -> JSON String -> JsonElement
            Map<String, Object> map = ConfigToJsonConverter.sectionToMap(componentSection);
            JsonElement jsonElement = GSON.fromJson(GSON.toJson(map), JsonElement.class);

            // 3. Use the real ComponentPatch.CODEC to parse the JSON. No reflection needed!
            // This is the magic of Paperweight.
            DataResult<DataComponentPatch> result = DataComponentPatch.CODEC.parse(JsonOps.INSTANCE, jsonElement);
            Optional<DataComponentPatch> patchOptional = result.result();

            // 4. Check if parsing was successful and apply the patch.
            if (patchOptional.isPresent()) {
                // The applyComponents method is on the modern ItemMeta interface.
                // This is clean, safe, and doesn't require casting to CraftMetaItem.
                craftMeta.handle.applyComponents(patchOptional.get());
            } else {
                // If parsing failed, provide a helpful error message to the user.
                String errorMessage = result.error()
                        .map(DataResult.Error::message) // If the error exists, get its message
                        .orElse("Unknown parsing error");        // Otherwise, use this default string
                Bukkit.getLogger().warning("[ItemsAPI] Failed to parse item components section: " + errorMessage);

            }

        } catch (Exception e) {
            // Catch any unexpected errors during the process.
            Bukkit.getLogger().severe("[ItemsAPI] A critical error occurred while applying item components.");
            e.printStackTrace();
        }
    }
}
