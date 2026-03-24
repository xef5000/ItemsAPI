package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.OptionalLong;

public class TokenManagerCurrencyIntegration implements CurrencyIntegration {

    private final TokenManager api;


    public TokenManagerCurrencyIntegration() {
        if (Bukkit.getServer().getPluginManager().getPlugin("TokenManager") == null) {
            throw new RuntimeException("CoinsEngine is not installed, cannot use TokenManager currency integration.");
        }
        api = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
    }

    @Override
    public double getBalance(Player player) {
        OptionalLong balance = api.getTokens(player);
        if (balance.isPresent())
            return balance.getAsLong();
        return 0;
    }

    @Override
    public void deposit(Player player, double amount) {
        api.addTokens(player, (long) amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        api.removeTokens(player, (long) amount);
    }
}
