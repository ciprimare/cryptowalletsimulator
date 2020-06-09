package com.cryptowallet.simulator.repository;

import java.util.Set;

public interface CurrencyRepository {
    Set<String> getSupportedCryptoCurrencies();

    void saveSupportedCryptoCurrencies(Set<String> cryptoCurrencies);
}
