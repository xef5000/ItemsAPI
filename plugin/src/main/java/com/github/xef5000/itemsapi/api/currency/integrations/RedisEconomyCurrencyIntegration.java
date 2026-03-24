package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import dev.unnm3d.rediseconomy.api.RedisEconomyAPI;
import dev.unnm3d.rediseconomy.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class RedisEconomyCurrencyIntegration implements CurrencyIntegration {

    private final Currency currency;
    private final RedisEconomyAPI economyApi;

    public RedisEconomyCurrencyIntegration(String currencyName) {
        if (!Bukkit.getPluginManager().isPluginEnabled("CoinsEngine")) {
            throw new RuntimeException("CoinsEngine is not installed, cannot use CoinsEngine currency integration.");
        }
        economyApi = RedisEconomyAPI.getAPI();

        this.currency = economyApi.getCurrencyByName(currencyName);
        if (currency == null)
            throw new RuntimeException("CoinsEngine currency not found: ");
    }

    @Override
    public double getBalance(Player player) {
        return currency.getBalance(player.getUniqueId());
    }

    @Override
    public void deposit(Player player, double amount) {
        currency.depositPlayer(player, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        currency.withdrawPlayer(player, amount);
    }
}
