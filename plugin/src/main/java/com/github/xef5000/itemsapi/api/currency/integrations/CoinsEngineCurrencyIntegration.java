package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI;
import su.nightexpress.excellenteconomy.api.currency.ExcellentCurrency;

public class CoinsEngineCurrencyIntegration implements CurrencyIntegration {

    private final ExcellentCurrency currency;
    private final ExcellentEconomyAPI economyApi;

    public CoinsEngineCurrencyIntegration(String currencyName) {
        if (!Bukkit.getPluginManager().isPluginEnabled("CoinsEngine")) {
            throw new RuntimeException("CoinsEngine is not installed, cannot use CoinsEngine currency integration.");
        }
        RegisteredServiceProvider<ExcellentEconomyAPI> rsp =
                Bukkit.getServer().getServicesManager().getRegistration(ExcellentEconomyAPI.class);

        if (rsp == null)
            throw new RuntimeException("CoinsEngine is not installed, cannot use CoinsEngine currency integration.");
        economyApi = rsp.getProvider();

        this.currency = economyApi.getCurrency(currencyName);
        if (currency == null)
            throw new RuntimeException("CoinsEngine currency not found: ");
    }


    @Override
    public double getBalance(Player player) {
        return economyApi.getBalance(player, currency);
    }

    @Override
    public void deposit(Player player, double amount) {
        economyApi.deposit(player, currency, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        economyApi.withdraw(player, currency, amount);
    }
}
