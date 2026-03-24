package com.github.xef5000.itemsapi.api;

import com.github.xef5000.itemsapi.api.currency.Currency;
import com.github.xef5000.itemsapi.api.currency.CurrencyIntegration;
import com.github.xef5000.itemsapi.api.currency.IntegrationFactory;
import com.github.xef5000.itemsapi.api.currency.integrations.*;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ItemsAPICurrency {

    private static final Map<String, IntegrationFactory> integrationFactories = new ConcurrentHashMap<>();

    @Nullable
    public static Currency fromConfigurationSection(ConfigurationSection currencyConfig) {
        if (currencyConfig == null || !currencyConfig.getBoolean("enabled", true)) {
            return null; // Skip if section is invalid or disabled
        }

        String integrationType = currencyConfig.getString("integration");
        if (integrationType == null) {
            return null;
        }

        Optional<CurrencyIntegration> integrationOpt = createIntegration(integrationType, currencyConfig);

        AtomicReference<Currency> currency = new AtomicReference<>();
        integrationOpt.ifPresent(integration -> {
            currency.set(new Currency(
                    currencyConfig.getName(),
                    currencyConfig.getString("symbol", "$"),
                    integration
            ));
        });
        return currency.get();
    }

    public static void registerDefaults() {
        // Integrations with no parameters
        registerIntegration("VAULT", config -> new VaultCurrencyIntegration());
        registerIntegration("PLAYERPOINTS", config -> new PlayerPointsCurrencyIntegration());
        registerIntegration("TOKENMANAGER", config -> new TokenManagerCurrencyIntegration());
        registerIntegration("TOKENENCHANT", config -> new TokenEnchantCurrencyIntegration());

        // Integrations WITH parameters
        registerIntegration("COINSENGINE", config -> {
            // The factory's logic now cleanly extracts its own required parameters.
            String currencyName = config.getString("currency-name");
            if (currencyName == null || currencyName.isEmpty()) {
                throw new IllegalArgumentException("The 'currency-name' key is missing in the config for this CoinsEngine currency.");
            }
            return new CoinsEngineCurrencyIntegration(currencyName);
        });

        registerIntegration("ULTRAECONOMY", config -> {
            String currencyName = config.getString("currency-name");
            if (currencyName == null || currencyName.isEmpty()) {
                throw new IllegalArgumentException("The 'currency-name' key is missing in the config for this UltraEconomy currency.");
            }
            return new UltraEconomyCurrencyIntegration(currencyName);
        });

        registerIntegration("CUSTOM", config -> {
            String placeholder = config.getString("balance-placeholder");
            String depositCommand = config.getString("deposit-command");
            String withdrawCommand = config.getString("withdraw-command");
            if (placeholder == null || depositCommand == null || withdrawCommand == null) {
                throw new IllegalArgumentException("Missing required parameters for custom currency integration.");
            }
            return new CustomCurrencyIntegration(placeholder, depositCommand, withdrawCommand);
        });
    }

    public static void registerIntegration(@NotNull String name, @NotNull IntegrationFactory factory) {
        integrationFactories.putIfAbsent(name.toUpperCase(), factory);
    }

    private static Optional<CurrencyIntegration> createIntegration(@NotNull String name, @NotNull ConfigurationSection config) {
        IntegrationFactory factory = integrationFactories.get(name.toUpperCase());
        if (factory == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(factory.create(config));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    static {
        registerDefaults();
    }
}
