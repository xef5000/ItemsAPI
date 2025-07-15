package com.github.xef5000.itemsapi.api;

import com.github.xef5000.itemsapi.api.adapter.ItemMetaAdapter;
import com.github.xef5000.itemsapi.api.adapter.LegacyItemMetaAdapter;
import com.github.xef5000.itemsapi.api.adapter.ModernItemMetaAdapter;
import com.github.xef5000.itemsapi.api.util.ServerVersion;
import com.github.xef5000.itemsapi.nms.legacy.LegacyNmsAdapter;
import com.github.xef5000.itemsapi.nms.modern.ModernNmsAdapter;
import org.bukkit.Bukkit;

public class VersionFactory {
    private static final ItemMetaAdapter itemMetaAdapter;
    private static final NMSAdapter nmsAdapter;

    static {
        String serverVersion = Bukkit.getBukkitVersion();
        // Simple version detection
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        // Poor man's version comparison, but effective for major versions
        int major = Integer.parseInt(version.split("\\.")[1]);

        if (major >= 14) {
            itemMetaAdapter = new ModernItemMetaAdapter();
        } else {
            itemMetaAdapter = new LegacyItemMetaAdapter();
        }
        if (ServerVersion.CURRENT.isAtLeast(1, 20, 5)) {
            nmsAdapter = new ModernNmsAdapter();
            Bukkit.getLogger().info("[ItemsAPI] Detected modern server version (1.20.5+). Full component support enabled.");
        } else {
            nmsAdapter = new LegacyNmsAdapter();
            Bukkit.getLogger().info("[ItemsAPI] Detected legacy server version. Component support is disabled.");
        }
    }

    public static NMSAdapter getNmsAdapter() {
        return nmsAdapter;
    }

    public static ItemMetaAdapter getItemMetaAdapter() {
        return itemMetaAdapter;
    }
}
