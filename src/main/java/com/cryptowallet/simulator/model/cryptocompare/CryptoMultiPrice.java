package com.cryptowallet.simulator.model.cryptocompare;

import com.cryptowallet.simulator.deserializer.CryptoMultiPriceDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = CryptoMultiPriceDeserializer.class)
public class CryptoMultiPrice {
    private List<CryptoPriceNode> prices;

    public List<CryptoPriceNode> getPrices() {
        return prices;
    }

    public void setPrices(List<CryptoPriceNode> prices) {
        this.prices = prices;
    }
}
