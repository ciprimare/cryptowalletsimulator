package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.config.CryptoCompareConfig;
import com.cryptowallet.simulator.exception.ExchangeRateNotFoundException;
import com.cryptowallet.simulator.model.cryptocompare.CryptoMultiPrice;
import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntry;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;
import com.cryptowallet.simulator.util.WalletUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;

@Component
public class WalletDao implements WalletRepository {
    private final Set<Wallet> wallets = new HashSet<>();

    private final RestTemplate restTemplate;
    private final CryptoCompareConfig cryptoCompareConfig;
    private final WalletUtil walletUtil;

    public WalletDao(RestTemplate restTemplate,
                     CryptoCompareConfig cryptoCompareConfig,
                     WalletUtil walletUtil) {
        this.restTemplate = restTemplate;
        this.cryptoCompareConfig = cryptoCompareConfig;
        this.walletUtil = walletUtil;
    }

    @Override
    public Set<Wallet> getWallets() {
        return wallets;
    }

    @Override
    public Wallet saveOrUpdateWallet(Wallet wallet, Set<String> cryptoCurrencies) {
        walletUtil.validateWalletName(wallets, wallet.getName());
        walletUtil.validateWalletCryptoCurrencies(cryptoCurrencies, wallet.getEntries());
        return saveOrUpdate(wallet);
    }

    @Override
    public Wallet getWalletByUuid(String uuid) {
        return walletUtil.getWalletOrThrowNotFoundException(wallets, uuid);
    }

    @Override
    public boolean deleteWallet(String uuid) {
        final Wallet walletToDelete = walletUtil.getWalletOrThrowNotFoundException(wallets, uuid);
        return wallets.remove(walletToDelete);
    }

    @Override
    public Wallet exchangeCryptoCurrency(String fromWalletId,
                                         String toWalletId,
                                         WalletEntryTransaction walletEntryTransaction,
                                         Set<String> supportedCryptoCurrencies) {

        final UUID fromWalletUuid = UUID.fromString(fromWalletId);

        final WalletEntry transactionFrom = walletEntryTransaction.getFrom();
        transactionFrom.setWalletId(fromWalletUuid);

        final WalletEntry transactionTo = walletEntryTransaction.getTo();
        transactionTo.setWalletId(StringUtils.isEmpty(toWalletId) ? fromWalletUuid : UUID.fromString(toWalletId));
        final UUID toWalletUuid = transactionTo.getWalletId();

        final Wallet fromWallet = walletUtil.getWalletOrThrowNotFoundException(wallets, fromWalletId);
        final Wallet toWallet = fromWalletUuid.equals(toWalletUuid) ? null : walletUtil.getWalletOrThrowNotFoundException(wallets, toWalletId);

        walletUtil.validateWalletEligibilityForExchangeCurrency(fromWallet, transactionFrom);
        walletUtil.validateWalletCryptoCurrencies(supportedCryptoCurrencies, new HashSet<>(singletonList(transactionTo)));

        final BigDecimal exchangeRate = getExchangeRateFromCryptoCompare(transactionFrom.getCurrency(), transactionTo.getCurrency());
        transactionTo.setAmount(transactionFrom.getAmount().multiply(exchangeRate));

        if (toWallet == null) {
            return saveOrUpdate(walletUtil.updateWalletWithEligibleTransactions(fromWallet, transactionFrom, transactionTo));
        }

        saveOrUpdate(walletUtil.updateWalletWithEligibleTransactions(fromWallet, transactionFrom, null));
        return saveOrUpdate(walletUtil.updateWalletWithEligibleTransactions(toWallet, null, transactionTo));
    }

    private BigDecimal getExchangeRateFromCryptoCompare(String fsym, String tsym) {
        final String url = cryptoCompareConfig.getUrl() + format(cryptoCompareConfig.getPrice(), fsym, tsym);
        final CryptoMultiPrice price = restTemplate.getForObject(url, CryptoMultiPrice.class);
        if (price == null || price.getPrices() == null || price.getPrices().isEmpty()) {
            throw new ExchangeRateNotFoundException(fsym, tsym);
        }
        return BigDecimal.valueOf(price.getPrices().get(0).getPrices().get(0).getPrice());
    }

    private Wallet saveOrUpdate(Wallet wallet) {
        if (wallet.getId() == null) {
            wallet.setId(UUID.randomUUID());
        } else {
            final String uuid = wallet.getId().toString();
            final Wallet walletToReplace = walletUtil.getWalletOrThrowNotFoundException(wallets, uuid);
            wallets.remove(walletToReplace);
        }
        wallets.add(wallet);
        return wallet;
    }
}
