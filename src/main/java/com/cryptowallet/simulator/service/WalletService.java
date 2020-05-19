package com.cryptowallet.simulator.service;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;

import java.util.Set;

public interface WalletService {
    Set<String> getAllCryptoCurrencies();

    Set<Wallet> getAllWallets();

    Wallet createWallet(Wallet wallet);

    Wallet getWallet(String uuid);

    Wallet updateWallet(Wallet wallet);

    boolean deleteWallet(String uuid);

    Wallet exchange(String fromWalletId, String toWalletId, WalletEntryTransaction walletEntryTransaction);
}
