package com.cryptowallet.simulator.service;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntryTransaction;
import com.cryptowallet.simulator.repository.CurrencyRepository;
import com.cryptowallet.simulator.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyRepository currencyRepository;

    public WalletServiceImpl(WalletRepository walletRepository, CurrencyRepository currencyRepository) {
        this.walletRepository = walletRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Set<Wallet> getAllWallets() {
        return walletRepository.getWallets();
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.saveOrUpdateWallet(wallet, currencyRepository.getSupportedCryptoCurrencies());
    }

    @Override
    public Wallet getWallet(String uuid) {
        return walletRepository.getWalletByUuid(uuid);
    }

    @Override
    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.saveOrUpdateWallet(wallet, currencyRepository.getSupportedCryptoCurrencies());
    }

    @Override
    public boolean deleteWallet(String uuid) {
        return walletRepository.deleteWallet(uuid);
    }

    @Override
    public Wallet exchange(String fromWalletId, String toWalletId, WalletEntryTransaction walletEntryTransaction) {
        return walletRepository.exchangeCryptoCurrency(fromWalletId, toWalletId, walletEntryTransaction, currencyRepository.getSupportedCryptoCurrencies());
    }
}
