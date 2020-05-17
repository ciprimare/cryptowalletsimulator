package com.cryptowallet.simulator.repository;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class WalletDao implements WalletRepository {
    private final Set<String> cryptoCurrencies = new HashSet<>();


    @Override
    public void saveCryptoCurrencies(Set<String> cryptoCurrencies) {
        this.cryptoCurrencies.clear();
        this.cryptoCurrencies.addAll(cryptoCurrencies);
    }

    @Override
    public Set<String> getCryptoCurrencies() {
        return cryptoCurrencies;
    }
}
