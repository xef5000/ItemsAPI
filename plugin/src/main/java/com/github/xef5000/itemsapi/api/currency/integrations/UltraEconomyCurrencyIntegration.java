package com.github.xef5000.itemsapi.api.currency.integrations;

import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.UltraEconomyAPI;
import me.TechsCode.UltraEconomy.objects.Account;
import me.TechsCode.UltraEconomy.objects.Currency;
import org.bukkit.entity.Player;

import java.util.Optional;

public class UltraEconomyCurrencyIntegration implements CurrencyIntegration {

    private final UltraEconomyAPI api;

    private final Currency currency;

    public UltraEconomyCurrencyIntegration(String currencyName) {
        UltraEconomyAPI api = UltraEconomy.getAPI();
        if (api == null) {
            throw new RuntimeException("UltraEconomy is not installed, cannot use UltraEconomy currency integration.");
        }
        this.api = api;
        for (Currency currency : api.getCurrencies()) {
            if (currency.getName().equalsIgnoreCase(currencyName)) {
                this.currency = currency;
                return;
            }
        }
        throw new RuntimeException("UltraEconomy currency not found: " + currencyName);
    }

    @Override
    public double getBalance(Player player) {
        Optional<Account> account = api.getAccounts().uuid(player.getUniqueId());
        if (account.isPresent()) {
            return account.get().getBalance(currency).getOnBank();
        }
        throw new RuntimeException("UltraEconomy account not found for player: " + player.getName());
    }

    @Override
    public void deposit(Player player, double amount) {
        Optional<Account> account = api.getAccounts().uuid(player.getUniqueId());
        account.ifPresent(value -> value.getBalance(currency).addBank(amount));
        throw new RuntimeException("UltraEconomy account not found for player: " + player.getName());
    }

    @Override
    public void withdraw(Player player, double amount) {
        Optional<Account> account = api.getAccounts().uuid(player.getUniqueId());
        account.ifPresent(value -> value.getBalance(currency).removeBank(amount));
        throw new RuntimeException("UltraEconomy account not found for player: " + player.getName());
    }
}
