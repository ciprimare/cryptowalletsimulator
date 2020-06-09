package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;

import java.util.Set;

public interface WalletRepository {
    Set<Wallet> getWallets();

    Wallet saveOrUpdateWallet(Wallet wallet, Set<String> cryptoCurrencies);

    Wallet getWalletByUuid(String uuid);

    boolean deleteWallet(String uuid);

    Wallet exchangeCryptoCurrency(String fromWalletId, String toWalletID, WalletEntryTransaction walletEntryTransaction, Set<String> cryptoCurrencies);
}
