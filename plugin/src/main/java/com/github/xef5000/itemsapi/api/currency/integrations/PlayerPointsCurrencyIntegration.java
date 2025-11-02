package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerPointsCurrencyIntegration implements CurrencyIntegration {

    private PlayerPointsAPI ppAPI;

    public PlayerPointsCurrencyIntegration() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppAPI = PlayerPoints.getInstance().getAPI();
        } else {
            throw new RuntimeException("PlayerPoints is not installed, cannot use PlayerPoints currency integration.");
        }
    }

    @Override
    public double getBalance(Player player) {
        return ppAPI.look(player.getUniqueId());
    }

    @Override
    public void deposit(Player player, double amount) {
        ppAPI.give(player.getUniqueId(), (int) amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        ppAPI.take(player.getUniqueId(), (int) amount);
    }
}
