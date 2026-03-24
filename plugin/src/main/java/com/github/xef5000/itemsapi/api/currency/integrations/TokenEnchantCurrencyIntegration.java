package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import com.vk2gpz.tokenenchant.api.ITokenEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TokenEnchantCurrencyIntegration implements CurrencyIntegration {

    private final ITokenEnchant api;

    public TokenEnchantCurrencyIntegration() {
        if (Bukkit.getServer().getPluginManager().getPlugin("TokenEnchant") == null) {
            throw new RuntimeException("TokenEnchant is not installed, cannot use TokenEnchant currency integration.");
        }
        this.api = (ITokenEnchant) Bukkit.getServer().getPluginManager().getPlugin("TokenEnchant");
    }

    @Override
    public double getBalance(Player player) {
        return api.getTokens(player);
    }

    @Override
    public void deposit(Player player, double amount) {
        api.addTokens(player, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        api.removeTokens(player, amount);
    }
}
