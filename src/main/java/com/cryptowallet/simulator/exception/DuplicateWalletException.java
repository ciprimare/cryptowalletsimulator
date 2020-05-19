package com.cryptowallet.simulator.exception;

public class DuplicateWalletException extends RuntimeException {
    public DuplicateWalletException(String walletName) {
        super("Wallet with walletName [" + walletName + "] already exist! Please choose a different wallet name.");
    }
}
