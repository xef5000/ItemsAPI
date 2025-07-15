package com.github.xef5000.itemsapi.api.material;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Base64MaterialHandler implements MaterialHandler {
    @Override
    public String getPrefix() {
        return "base64";
    }

    @Override
    public ItemStack parse(String data) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID()); // New API 1.18+
        PlayerTextures textures = profile.getTextures();
        try {
            URL url = new URL("https://textures.minecraft.net/texture/" + data);
            textures.setSkin(url);
        } catch (MalformedURLException e) {
            // Handle error, maybe return a default steve head
            e.printStackTrace();
        }
        profile.setTextures(textures);
        assert meta != null;
        meta.setOwnerProfile(profile);

        head.setItemMeta(meta);
        return head;
    }
}
