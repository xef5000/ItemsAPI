package com.github.xef5000.itemsapi.nms.modern;

import com.github.xef5000.itemsapi.api.NMSAdapter;
import com.github.xef5000.itemsapi.api.util.ConfigToJsonConverter;
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
                // Get the actual class of the ItemMeta object (e.g., CraftMetaItem, CraftMetaSkull, etc.).
                Class<?> metaClass = meta.getClass();

                Class<?> currentClass = metaClass;

                while (currentClass != null && updateFromPatchMethod == null) {
                    try {
                        // Try to find the method in the current class
                        updateFromPatchMethod = currentClass.getDeclaredMethod(
                                "updateFromPatch",
                                DataComponentPatch.class,
                                Set.class
                        );
                    } catch (NoSuchMethodException e) {
                        // Method not found in this class, try the parent class
                        currentClass = currentClass.getSuperclass();
                    }
                }

                if (updateFromPatchMethod == null) {
                    throw new NoSuchMethodException(
                            "updateFromPatch method not found in " + metaClass.getName() + " or any of its superclasses"
                    );
                }

                // Make it accessible even if it's protected/private
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
