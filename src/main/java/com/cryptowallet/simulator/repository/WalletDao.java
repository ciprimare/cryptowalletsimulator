package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.exception.DuplicateWalletException;
import com.cryptowallet.simulator.exception.WalletCurrencyNotSupportedException;
import com.cryptowallet.simulator.exception.WalletNotFoundException;
import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
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
        if (isNewWallet) {
            validateWalletName(wallet.getName());
            validateWalletCurrencies(wallet.getEntries());
            wallet.setId(UUID.randomUUID());
            wallets.add(wallet);
        } else {
            // we should update the wallet with the new entries
        }
        return wallet;
    }

    @Override
    public Wallet getWalletByUuid(String uuid) {
        return findWalletByUuid(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }

    @Override
    public boolean deleteWallet(String uuid) {
        // look for uuid in set if found remove it if not return wallet not found error
        return false;
    }

    // private methods
    private void validateWalletName(String name) {
        final Optional<Wallet> optionalExistingWallet = findWalletByName(name);
        optionalExistingWallet.ifPresent(wallet -> {
            throw new DuplicateWalletException(name);
        });
    }

    private Optional<Wallet> findWalletByName(String name) {
        return wallets
                .stream()
                .filter(wallet -> wallet.getName().equals(name))
                .findFirst();
    }

    private void validateWalletCurrencies(Set<WalletEntry> entries) {
        final Set<String> unsupportedCurrencies = new HashSet<>();
        entries
                .stream()
                .map(WalletEntry::getCurrency)
                .forEach(walletCurrency -> {
                    if (!cryptoCurrencies.contains(walletCurrency)) {
                        unsupportedCurrencies.add(walletCurrency);
                    }
                });
        if (unsupportedCurrencies.size() > 0) {
            throw new WalletCurrencyNotSupportedException(unsupportedCurrencies.toString());
        }
    }

    private Optional<Wallet> findWalletByUuid(String uuid) {
        return wallets
                .stream()
                .filter(wallet -> wallet.getId().toString().equals(uuid))
                .findFirst();
    }
}
