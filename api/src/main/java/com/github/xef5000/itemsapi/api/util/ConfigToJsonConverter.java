package com.github.xef5000.itemsapi.api.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility to convert Bukkit's ConfigurationSection into a standard Java Map
 * that can be easily serialized into a JSON string by libraries like Gson.
 * <p>
 * This is a necessary intermediate step before passing configuration data
 * to Mojang's serialization system (JsonOps).
 */
public final class ConfigToJsonConverter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ConfigToJsonConverter() {}

    /**
     * Recursively converts a Bukkit ConfigurationSection to a standard Java Map.
     *
     * @param section The ConfigurationSection to convert. Cannot be null.
     * @return A Map representing the data in the section.
     */
    public static Map<String, Object> sectionToMap(ConfigurationSection section) {
        // The 'false' argument gets only the top-level keys.
        // We handle deep conversion recursively via the convertValue method.
        return section.getValues(false).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> convertValue(entry.getValue())
                ));
    }

    /**
     * Converts a generic configuration value. If the value is a
     * ConfigurationSection or a List, it will be converted recursively.
     *
     * @param value The object value from the configuration.
     * @return A converted, JSON-friendly object.
     */
    private static Object convertValue(Object value) {
        if (value instanceof ConfigurationSection) {
            // If the value is another section, convert it to a map recursively.
            return sectionToMap((ConfigurationSection) value);
        }
        if (value instanceof List) {
            // If the value is a list, process each element in the list.
            return ((List<?>) value).stream()
                    .map(ConfigToJsonConverter::convertValue)
                    .collect(Collectors.toList());
        }
        // For simple types (String, Integer, Double, Boolean), return them as is.
        return value;
    }
}
