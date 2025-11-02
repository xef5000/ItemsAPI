package com.github.xef5000.itemsapi.api.currency;

import org.bukkit.entity.Player;

public interface CurrencyIntegration {
    double getBalance(Player player);
    void deposit(Player player, double amount);
    void withdraw(Player player, double amount);
}

