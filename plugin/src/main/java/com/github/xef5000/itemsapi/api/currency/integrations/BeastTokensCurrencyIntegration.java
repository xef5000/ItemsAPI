package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import me.mraxetv.beasttokens.api.BeastTokensAPI;
import org.bukkit.entity.Player;

public class BeastTokensCurrencyIntegration implements CurrencyIntegration {
    @Override
    public double getBalance(Player player) {
        return BeastTokensAPI.getTokensManager().getTokens(player);
    }

    @Override
    public void deposit(Player player, double amount) {
        BeastTokensAPI.getTokensManager().addTokens(player, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        BeastTokensAPI.getTokensManager().removeTokens(player, amount);
    }
}
