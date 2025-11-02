package com.github.xef5000.itemsapi.api.currency;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * A factory responsible for creating a CurrencyIntegration instance.
 * It receives the specific configuration section for a currency, allowing it
 * to pull any required parameters for its construction.
 */
@FunctionalInterface
public interface IntegrationFactory {

    /**
     * Creates a new instance of a CurrencyIntegration.
     *
     * @param config The {@link ConfigurationSection} for this specific currency
     *               (e.g., the 'currencies.MyVaultCurrency' section).
     * @return A fully constructed {@link CurrencyIntegration} instance.
     * @throws RuntimeException if the integration cannot be created (e.g., missing dependency or invalid config).
     */
    @NotNull
    CurrencyIntegration create(@NotNull ConfigurationSection config);

}
