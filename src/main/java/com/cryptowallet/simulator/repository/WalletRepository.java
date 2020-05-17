package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.model.wallet.Wallet;

import java.util.Set;

public interface WalletRepository {
    void saveCryptoCurrencies(Set<String> cryptoCurrencies);

    Set<String> getCryptoCurrencies();

    Set<Wallet> getWallets();
}
