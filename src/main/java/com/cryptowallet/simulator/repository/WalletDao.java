package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.exception.WalletNotFoundException;
import com.cryptowallet.simulator.model.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @Override
    public Wallet saveOrUpdate(Wallet wallet) {
        final boolean isNewWallet = wallet.getId() == null;
        // we should have unique names so iterate through the list of wallets
        // and if name already exist throw a custom exception to change the name
        if (isNewWallet) {
            wallet.setId(UUID.randomUUID());
            wallets.add(wallet);
        } else {
            // we should update the wallet with the new entries
        }
        return wallet;
    }

    @Override
    public Wallet getWalletByUuid(String uuid) {
        return wallets
                .stream()
                .filter(wallet -> wallet.getId().toString().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }

    @Override
    public boolean deleteWallet(String uuid) {
        // look for uuid in set if found remove it if not return wallet not found error
        return false;
    }
}
