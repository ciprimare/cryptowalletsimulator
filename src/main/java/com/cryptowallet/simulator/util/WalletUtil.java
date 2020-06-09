package com.cryptowallet.simulator.util;

import com.cryptowallet.simulator.exception.DuplicateWalletException;
import com.cryptowallet.simulator.exception.WalletCurrencyNotSupportedException;
import com.cryptowallet.simulator.exception.WalletNotEligibleAmountForExchangeException;
import com.cryptowallet.simulator.exception.WalletNotEligibleCurrencyForExchangeException;
import com.cryptowallet.simulator.exception.WalletNotFoundException;
import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntry;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WalletUtil {

    public Optional<Wallet> findWalletByName(Set<Wallet> wallets, String name) {
        return wallets
                .stream()
                .filter(wallet -> wallet.getName().equals(name))
                .findFirst();
    }

    public Optional<WalletEntry> findWalletEntryByCurrency(Wallet wallet, String currency) {
        return wallet.getEntries()
                .stream()
                .filter(walletEntry -> walletEntry.getCurrency().equals(currency))
                .findFirst();
    }

    public Optional<Wallet> findWalletByUuid(Set<Wallet> wallets, String uuid) {
        return wallets
                .stream()
                .filter(wallet -> wallet.getId().toString().equals(uuid))
                .findFirst();
    }

    public Wallet getWalletOrThrowNotFoundException(Set<Wallet> wallets, String uuid) {
        return findWalletByUuid(wallets, uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }

    public void validateWalletName(Set<Wallet> wallets, String name) {
        final Optional<Wallet> optionalExistingWallet = findWalletByName(wallets, name);
        optionalExistingWallet.ifPresent(wallet -> {
            throw new DuplicateWalletException(name);
        });
    }

    public Wallet updateWalletWithEligibleTransactions(
            Wallet wallet,
            WalletEntry transactionFrom,
            WalletEntry transactionTo
    ) {
        final Set<WalletEntry> entries = wallet.getEntries()
                .stream()
                .map(walletEntry -> updateWalletEntryIfExist(walletEntry, transactionFrom, transactionTo))
                .filter(walletEntry -> walletEntry.getAmount().doubleValue() > 0)
                .collect(Collectors.toSet());
        if (transactionTo != null) {
            entries.add(transactionTo); // this should cover only the case when this currency is not already in the wallet
        }
        wallet.setEntries(entries);
        return wallet;
    }

    public void validateWalletEligibilityForExchangeCurrency(Wallet wallet, WalletEntry transactionFrom) {
        final WalletEntry eligibleWalletEntry =
                findWalletEntryByCurrency(wallet, transactionFrom.getCurrency())
                        .orElseThrow(() -> new WalletNotEligibleCurrencyForExchangeException(
                                wallet.getId().toString(),
                                transactionFrom.getCurrency()));
        if (transactionFrom.getAmount().compareTo(eligibleWalletEntry.getAmount()) > 0) {
            throw new WalletNotEligibleAmountForExchangeException(
                    wallet.getId().toString(),
                    transactionFrom.getAmount(),
                    eligibleWalletEntry.getAmount());
        }
    }

    public void validateWalletCryptoCurrencies(Set<String> supportedCryptoCurrencies, Set<WalletEntry> entries) {
        final Set<String> unsupportedCurrencies = getUnsupportedCryptoCurrencies(supportedCryptoCurrencies, entries);
        if (unsupportedCurrencies.size() > 0) {
            throw new WalletCurrencyNotSupportedException(unsupportedCurrencies.toString());
        }
    }

    private Set<String> getUnsupportedCryptoCurrencies(Set<String> supportedCryptoCurrencies, Set<WalletEntry> entries) {
        final Set<String> unsupportedCryptoCurrencies = new HashSet<>();
        entries
                .stream()
                .map(WalletEntry::getCurrency)
                .forEach(walletCurrency -> {
                    if (!supportedCryptoCurrencies.contains(walletCurrency)) {
                        unsupportedCryptoCurrencies.add(walletCurrency);
                    }
                });
        return unsupportedCryptoCurrencies;
    }

    private WalletEntry updateWalletEntryIfExist(
            WalletEntry entryToUpdate,
            WalletEntry transactionFrom,
            WalletEntry transactionTo) {
        if (transactionFrom != null && entryToUpdate.getCurrency().equals(transactionFrom.getCurrency())) {
            entryToUpdate.setAmount(entryToUpdate.getAmount().subtract(transactionFrom.getAmount()));
        }
        if (transactionTo != null && entryToUpdate.getCurrency().equals(transactionTo.getCurrency())) {
            entryToUpdate.setAmount(entryToUpdate.getAmount().add(transactionTo.getAmount()));
        }
        return entryToUpdate;
    }
}
