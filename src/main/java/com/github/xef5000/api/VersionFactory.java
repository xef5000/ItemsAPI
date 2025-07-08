package com.github.xef5000.api;

import com.github.xef5000.api.adapter.ItemMetaAdapter;
import com.github.xef5000.api.adapter.LegacyItemMetaAdapter;
import com.github.xef5000.api.adapter.ModernItemMetaAdapter;
import org.bukkit.Bukkit;

public class VersionFactory {
    private static final ItemMetaAdapter itemMetaAdapter;

    static {
        // Simple version detection
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        // Poor man's version comparison, but effective for major versions
        int major = Integer.parseInt(version.split("\\.")[1]);

        if (major >= 14) {
            itemMetaAdapter = new ModernItemMetaAdapter();
        } else {
            itemMetaAdapter = new LegacyItemMetaAdapter();
        }
    }

    public static ItemMetaAdapter getItemMetaAdapter() {
        return itemMetaAdapter;
    }
}
