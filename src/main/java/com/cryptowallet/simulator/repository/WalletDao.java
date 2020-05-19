package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.config.CryptoCompareConfig;
import com.cryptowallet.simulator.exception.*;
import com.cryptowallet.simulator.model.cryptocompare.CryptoMultiPrice;
import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntry;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;

@Component
public class WalletDao implements WalletRepository {
    private final Set<String> cryptoCurrencies = new HashSet<>();
    private final Set<Wallet> wallets = new HashSet<>();

    @Autowired
    WalletMockDataGenerator walletMockDataGenerator;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CryptoCompareConfig cryptoCompareConfig;

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
    public Wallet saveOrUpdateWallet(Wallet wallet) {
        validateWalletName(wallet.getName());
        validateWalletCurrencies(wallet.getEntries());
        return saveOrUpdate(wallet);
    }

    @Override
    public Wallet getWalletByUuid(String uuid) {
        return getWalletOrThrowNotFoundException(uuid);
    }

    @Override
    public boolean deleteWallet(String uuid) {
        final Wallet walletToDelete = getWalletOrThrowNotFoundException(uuid);
        return wallets.remove(walletToDelete);
    }

    @Override
    public Wallet exchangeCurrency(String fromWalletId,
                                   String toWalletId,
                                   WalletEntryTransaction walletEntryTransaction) {

        final UUID fromWalletUuid = UUID.fromString(fromWalletId);

        final WalletEntry transactionFrom = walletEntryTransaction.getFrom();
        transactionFrom.setWalletId(fromWalletUuid);

        final WalletEntry transactionTo = walletEntryTransaction.getTo();
        transactionTo.setWalletId(StringUtils.isEmpty(toWalletId) ? fromWalletUuid : UUID.fromString(toWalletId));
        final UUID toWalletUuid = transactionTo.getWalletId();

        final Wallet fromWallet = getWalletOrThrowNotFoundException(fromWalletId);
        final Wallet toWallet = fromWalletUuid.equals(toWalletUuid) ? null : getWalletOrThrowNotFoundException(toWalletId);

        validateWalletEligibilityForExchangeCurrency(fromWallet, transactionFrom);
        validateWalletCurrencies(new HashSet<>(singletonList(transactionTo)));

        final BigDecimal exchangeRate = getExchangeRateFromCryptoCompare(transactionFrom.getCurrency(), transactionTo.getCurrency());
        transactionTo.setAmount(transactionFrom.getAmount().multiply(exchangeRate));

        if (toWallet == null) {
            return saveOrUpdate(updateWalletWithEligibleTransactions(fromWallet, transactionFrom, transactionTo));
        }

        saveOrUpdate(updateWalletWithEligibleTransactions(fromWallet, transactionFrom, null));
        return saveOrUpdate(updateWalletWithEligibleTransactions(toWallet, null, transactionTo));
    }

    // private methods
    private Wallet updateWalletWithEligibleTransactions(
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

    private BigDecimal getExchangeRateFromCryptoCompare(String fsym, String tsym) {
        final String url = cryptoCompareConfig.getUrl() + format(cryptoCompareConfig.getPrice(), fsym, tsym);
        final CryptoMultiPrice price = restTemplate.getForObject(url, CryptoMultiPrice.class);
        return getFirstExchangeRateOrThrowException(price, fsym, tsym);
    }

    private BigDecimal getFirstExchangeRateOrThrowException(CryptoMultiPrice price, String fsym, String tsym) {
        if (price == null || price.getPrices() == null || price.getPrices().isEmpty()) {
            throw new ExchangeRateNotFoundException(fsym, tsym);
        }
        return BigDecimal.valueOf(price.getPrices().get(0).getPrices().get(0).getPrice());
    }

    private Optional<WalletEntry> findWalletEntryByCurrency(Wallet wallet, String currency) {
        return wallet.getEntries()
                .stream()
                .filter(walletEntry -> walletEntry.getCurrency().equals(currency))
                .findFirst();
    }

    private void validateWalletEligibilityForExchangeCurrency(Wallet wallet, WalletEntry transactionFrom) {
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

    private Wallet getWalletOrThrowNotFoundException(String uuid) {
        return findWalletByUuid(uuid)
                .orElseThrow(() -> new WalletNotFoundException(uuid));
    }

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

    private Wallet saveOrUpdate(Wallet wallet) {
        if (wallet.getId() == null) {
            wallet.setId(UUID.randomUUID());
        } else {
            final String uuid = wallet.getId().toString();
            final Wallet walletToReplace = getWalletOrThrowNotFoundException(uuid);
            wallets.remove(walletToReplace);
        }
        wallets.add(wallet);
        return wallet;
    }
}
