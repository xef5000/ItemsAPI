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
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModernNmsAdapter implements NMSAdapter {
    private static final Gson GSON = new Gson();

    private static Method updateFromPatchMethod = null;

    @Override
    public void applyComponents(ItemMeta meta, ConfigurationSection componentSection) {
        if (componentSection == null || meta == null) {
            return;
        }

        try {
            Map<String, Object> map = ConfigToJsonConverter.sectionToMap(componentSection);
            JsonElement jsonElement = GSON.fromJson(GSON.toJson(map), JsonElement.class);

            DataResult<DataComponentPatch> result = DataComponentPatch.CODEC.parse(JsonOps.INSTANCE, jsonElement);
            Optional<DataComponentPatch> patchOptional = result.result();

            if (patchOptional.isEmpty()) {
                String errorMessage = result.error()
                        .map(DataResult.Error::message)
                        .orElse("Unknown parsing error");
                Bukkit.getLogger().warning("[ItemsAPI] Failed to parse item components section: " + errorMessage);
                return;
            }

            if (updateFromPatchMethod == null) {
                // Get the actual class of the ItemMeta object (e.g., CraftMetaItem).
                Class<?> metaClass = meta.getClass();
                // Find the method on that class. It's named 'applyComponents' and takes one ComponentPatch argument.
                updateFromPatchMethod = metaClass.getDeclaredMethod("updateFromPatch", DataComponentPatch.class, Set.class);
                // Make it accessible even if it's not public (though it should be on the implementation).
                updateFromPatchMethod.setAccessible(true);
            }

            // 2. Invoke the method on our specific ItemMeta instance.
            updateFromPatchMethod.invoke(meta, patchOptional.get(), null);

        } catch (Exception e) {
            // Catch any unexpected errors during the process.
            Bukkit.getLogger().severe("[ItemsAPI] A critical error occurred while applying item components.");
            e.printStackTrace();
        }
    }
}
