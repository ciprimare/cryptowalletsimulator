package com.cryptowallet.simulator.model.cryptocompare;

import java.util.List;

public class CryptoPriceNode {

    private String name;

    private List<CryptoPrice> prices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CryptoPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<CryptoPrice> prices) {
        this.prices = prices;
    }
}
