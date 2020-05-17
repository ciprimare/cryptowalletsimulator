package com.cryptowallet.simulator.model.cryptocompare;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class CryptoCurrencies {
    @JsonProperty("Data")
    private HashMap<String, CryptoCurrency> coins;

    public HashMap<String, CryptoCurrency> getCoins() {
        return coins;
    }

    public void setCoins(HashMap<String, CryptoCurrency> coins) {
        this.coins = coins;
    }
}
