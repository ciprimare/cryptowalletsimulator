package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class WalletMockDataGenerator {

    public Set<Wallet> generateWallets() {
        final Set<Wallet> wallets = new HashSet<>();
        final Wallet firstWallet = new Wallet();
        firstWallet.setName("First Wallet for testing");
        firstWallet.setEntries(generateMockWalletEntries());
        wallets.add(firstWallet);
        final Wallet secondWallet = new Wallet();
        secondWallet.setName("Second Wallet for testing");
        secondWallet.setEntries(generateMockWalletEntries());
        wallets.add(secondWallet);
        return wallets;
    }

    private Set<WalletEntry> generateMockWalletEntries() {
        final Set<WalletEntry> entries = new HashSet<>();
        final WalletEntry btcEntry = new WalletEntry();
        btcEntry.setCurrency("BTC");
        btcEntry.setAmount(BigDecimal.valueOf(1000000));
        entries.add(btcEntry);
        final WalletEntry bbcEntry = new WalletEntry();
        bbcEntry.setCurrency("BBC");
        bbcEntry.setAmount(BigDecimal.valueOf(1000000));
        entries.add(bbcEntry);
        return entries;
    }
}
