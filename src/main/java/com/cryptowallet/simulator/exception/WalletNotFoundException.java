package com.cryptowallet.simulator.exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String walletId) {
        super("Could not find wallet with id = " + walletId);
    }
}
