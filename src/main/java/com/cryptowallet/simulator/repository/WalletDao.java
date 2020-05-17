package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.model.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class WalletDao implements WalletRepository {
    private final Set<String> cryptoCurrencies = new HashSet<>();
    private final Set<Wallet> wallets = new HashSet<>();

    @Autowired
    WalletMockDataGenerator walletMockDataGenerator;

    public WalletDao(WalletMockDataGenerator walletMockDataGenerator) {
        wallets.addAll(walletMockDataGenerator.generateWallets());
    }

    @Override
    public void saveCryptoCurrencies(Set<String> cryptoCurrencies) {
        this.cryptoCurrencies.clear();
        this.cryptoCurrencies.addAll(cryptoCurrencies);
    }

    @Override
    public Set<String> getCryptoCurrencies() {
        return cryptoCurrencies;
    }

    @Override
    public Set<Wallet> getWallets() {
        return wallets;
    }
}
