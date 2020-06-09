package com.cryptowallet.simulator.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CurrencyDao implements CurrencyRepository {


    private final Set<String> cryptoCurrencies = new HashSet<>();

    @Override
    public Set<String> getSupportedCryptoCurrencies() {
        return cryptoCurrencies;
    }

    @Override
    public void saveSupportedCryptoCurrencies(Set<String> cryptoCurrencies) {
        this.cryptoCurrencies.clear();
        this.cryptoCurrencies.addAll(cryptoCurrencies);
    }
}
