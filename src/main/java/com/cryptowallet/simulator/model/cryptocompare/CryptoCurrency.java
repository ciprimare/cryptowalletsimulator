package com.cryptowallet.simulator.model.cryptocompare;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CryptoCurrency {
    @JsonProperty("Symbol")
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
