package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import me.swanis.mobcoins.MobCoinsAPI;
import org.bukkit.entity.Player;

public class SuperMobCoinsCurrencyIntegration implements CurrencyIntegration {
    @Override
    public double getBalance(Player player) {
        return getMobCoins(player);
    }

    @Override
    public void deposit(Player player, double amount) {
        MobCoinsAPI.getProfileManager().getProfile(player).setMobCoins((long) (getMobCoins(player) + amount));
    }

    @Override
    public void withdraw(Player player, double amount) {
        MobCoinsAPI.getProfileManager().getProfile(player).setMobCoins((long) (getMobCoins(player) - amount));
    }

    private long getMobCoins(Player player) {
        return MobCoinsAPI.getProfileManager().getProfile(player).getMobCoins();
    }
}
