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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

            DataResult<DataComponentPatch> result = DataComponentPatch. CODEC.parse(JsonOps.INSTANCE, jsonElement);
            Optional<DataComponentPatch> patchOptional = result.result();

            if (patchOptional.isEmpty()) {
                String errorMessage = result.error()
                        .map(DataResult. Error::message)
                        .orElse("Unknown parsing error");
                Bukkit.getLogger().warning("[ItemsAPI] Failed to parse item components section: " + errorMessage);
                return;
            }

            DataComponentPatch patch = patchOptional.get();

            // Get the specific meta class (e.g., CraftMetaSkull, CraftMetaArmor, etc.)
            Class<? > metaClass = meta.getClass();

            // Find the constructor that takes (DataComponentPatch, Set)
            Constructor<?> constructor = null;
            try {
                constructor = metaClass.getDeclaredConstructor(DataComponentPatch.class, Set.class);
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // Try parent classes if not found
                Class<? > currentClass = metaClass.getSuperclass();
                while (currentClass != null && constructor == null) {
                    try {
                        constructor = currentClass.getDeclaredConstructor(DataComponentPatch.class, Set. class);
                        constructor.setAccessible(true);
                    } catch (NoSuchMethodException ignored) {
                        currentClass = currentClass.getSuperclass();
                    }
                }
            }

            if (constructor == null) {
                throw new NoSuchMethodException(
                        "Constructor(DataComponentPatch, Set) not found in " + metaClass.getName()
                );
            }

            // Create a new meta instance with the patch applied
            ItemMeta newMeta = (ItemMeta) constructor.newInstance(patch, null);

            // Now we need to merge the new meta back into the original meta
            // This is done by getting all the applied values from newMeta and setting them on meta
            mergeMetaInto(newMeta, meta);

        } catch (Exception e) {
            Bukkit.getLogger().severe("[ItemsAPI] A critical error occurred while applying item components.");
            e.printStackTrace();
        }
    }

    private void mergeMetaInto(ItemMeta source, ItemMeta target) throws Exception {
        Class<?> currentClass = source.getClass();

        // Dynamically get the CraftMetaItem class using the server's version package
        // This bypasses the "is not public" compile error.
        String obcPackage = Bukkit.getServer().getClass().getPackageName(); // e.g., org.bukkit.craftbukkit.v1_21_R1
        Class<?> craftMetaItemClass = Class.forName(obcPackage + ".inventory.CraftMetaItem");

        // Iterate up the class hierarchy until we go past CraftMetaItem
        while (currentClass != null && craftMetaItemClass.isAssignableFrom(currentClass)) {

            for (Field field : currentClass.getDeclaredFields()) {
                // Skip static fields
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(source);

                // Only copy non-null values to overwrite/merge into the target
                if (value != null) {
                    // Handle specific edge cases if necessary (e.g., Maps/Lists),
                    // but for direct NMS component patching, direct assignment is usually what we want.
                    field.set(target, value);
                }
            }

            currentClass = currentClass.getSuperclass();
        }
    }
}
