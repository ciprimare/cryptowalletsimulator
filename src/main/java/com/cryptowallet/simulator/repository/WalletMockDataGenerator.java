package com.cryptowallet.simulator.repository;

import com.cryptowallet.simulator.model.wallet.Wallet;
import com.cryptowallet.simulator.model.wallet.WalletEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class WalletMockDataGenerator {
    private static final String FIRST_UUID = "af412380-67c8-4175-87c5-04c6f8450d4e";
    private static final String SECOND_UUID = "de9b66d8-8471-4348-bca5-9417e4e028b7";

    public Set<Wallet> generateWallets() {
        final Set<Wallet> wallets = new HashSet<>();
        final Wallet firstWallet = new Wallet();
        final UUID firstWalletId = UUID.fromString(FIRST_UUID);
        firstWallet.setId(firstWalletId);
        firstWallet.setName("First Wallet for testing");
        firstWallet.setEntries(generateMockWalletEntries(firstWalletId));
        wallets.add(firstWallet);
        final Wallet secondWallet = new Wallet();
        final UUID secondWalletId = UUID.fromString(SECOND_UUID);
        secondWallet.setId(secondWalletId);
        secondWallet.setName("Second Wallet for testing");
        secondWallet.setEntries(generateMockWalletEntries(secondWalletId));
        wallets.add(secondWallet);
        return wallets;
    }

    private Set<WalletEntry> generateMockWalletEntries(UUID walletId) {
        final Set<WalletEntry> entries = new HashSet<>();
        final WalletEntry btcEntry = new WalletEntry();
        btcEntry.setWalletId(walletId);
        btcEntry.setCurrency("BTC");
        btcEntry.setAmount(BigDecimal.valueOf(1000000));
        entries.add(btcEntry);
        final WalletEntry bbcEntry = new WalletEntry();
        bbcEntry.setWalletId(walletId);
        bbcEntry.setCurrency("BBC");
        bbcEntry.setAmount(BigDecimal.valueOf(1000000));
        entries.add(bbcEntry);
        return entries;
    }
}
