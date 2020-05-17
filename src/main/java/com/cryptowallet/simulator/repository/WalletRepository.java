package com.cryptowallet.simulator.repository;

import java.util.Set;

public interface WalletRepository {
    void saveCryptoCurrencies(Set<String> cryptoCurrencies);

    Set<String> getCryptoCurrencies();
}
