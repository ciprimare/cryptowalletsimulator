package com.cryptowallet.simulator.service;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletRepository walletDao;

    @Override
    public Set<String> getAllCryptoCurrencies() {
        return walletDao.getCryptoCurrencies();
    }

    @Override
    public Set<Wallet> getAllWallets() {
        return walletDao.getWallets();
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletDao.saveOrUpdate(wallet);
    }

    @Override
    public Wallet getWallet(String uuid) {
        return walletDao.getWalletByUuid(uuid);
    }

    @Override
    public Wallet updateWallet(Wallet wallet) {
        return walletDao.saveOrUpdate(wallet);
    }

    @Override
    public boolean deleteWallet(String uuid) {
        return walletDao.deleteWallet(uuid);
    }
}
