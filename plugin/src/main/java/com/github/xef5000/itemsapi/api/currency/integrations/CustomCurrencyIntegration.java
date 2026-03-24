package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CustomCurrencyIntegration implements CurrencyIntegration {

    private final String placeholder; // Something like "%coinsengine_balance_plain_gems%"
    private final String depositCommand; // Something like "/gems give {player} {amount}"
    private final String withdrawCommand; // Something like "/gems take {player} {amount}"

    public CustomCurrencyIntegration(String placeholder, String depositCommand, String withdrawCommand) {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            throw new RuntimeException("PlaceholderAPI is not installed, cannot use custom currency integration.");
        }
        this.placeholder = placeholder;
        this.depositCommand = depositCommand;
        this.withdrawCommand = withdrawCommand;
    }

    @Override
    public double getBalance(Player player) {
        String balanceStr = PlaceholderAPI.setPlaceholders(player, placeholder);
        return Double.parseDouble(balanceStr);
    }

    @Override
    public void deposit(Player player, double amount) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), depositCommand
                .replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount)));
    }

    @Override
    public void withdraw(Player player, double amount) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), withdrawCommand
                .replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount)));
    }
}
