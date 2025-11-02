package com.github.xef5000.itemsapi.api.currency;

public class Currency {
    private String name;
    private String symbol;
    private CurrencyIntegration integration;

    public Currency(String name, String symbol, CurrencyIntegration integration) {
        this.name = name;
        this.symbol = symbol;
        this.integration = integration;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public CurrencyIntegration getIntegration() {
        return integration;
    }
}

