package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;

import java.util.Set;
import java.util.UUID;

public interface WalletRepository {
    void saveCryptoCurrencies(Set<String> cryptoCurrencies);

    Set<String> getCryptoCurrencies();

    Set<Wallet> getWallets();

    Wallet saveOrUpdateWallet(Wallet wallet);

    Wallet getWalletByUuid(String uuid);

    boolean deleteWallet(String uuid);

    Wallet exchangeCurrency(String fromWalletId, String toWalletID, WalletEntryTransaction walletEntryTransaction);
}
