package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.economy.Economy;

public class VaultCurrencyIntegration implements CurrencyIntegration {

    private static Economy economy;

    public VaultCurrencyIntegration() {
        if (!setupEconomy()) {
            throw new RuntimeException("Vault is not installed, cannot use Vault currency integration.");
        }
    }

    @Override
    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    @Override
    public void deposit(Player player, double amount) {
        economy.depositPlayer(player, amount);
    }

    @Override
    public void withdraw(Player player, double amount) {
        economy.withdrawPlayer(player, amount);
    }

    private boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
