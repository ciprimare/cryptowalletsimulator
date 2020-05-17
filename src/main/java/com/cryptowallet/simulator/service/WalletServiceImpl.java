package com.cryptowallet.simulator.service;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
