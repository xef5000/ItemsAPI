package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public class CoinsEngineCurrencyIntegration implements CurrencyIntegration {

    private final Currency currency;

    public CoinsEngineCurrencyIntegration(String currencyName) {
        if (!Bukkit.getPluginManager().isPluginEnabled("CoinsEngine")) {
            throw new RuntimeException("CoinsEngine is not installed, cannot use CoinsEngine currency integration.");
        }
        this.currency = CoinsEngineAPI.getCurrency(currencyName);
        if (currency == null)
            throw new RuntimeException("CoinsEngine currency not found: ");
    }


    @Override
    public double getBalance(Player player) {
        return CoinsEngineAPI.getBalance(player, currency);
    }

    @Override
    public void deposit(Player player, double amount) {
        CoinsEngineAPI.addBalance(player, currency, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        CoinsEngineAPI.removeBalance(player, currency, amount);
    }
}
