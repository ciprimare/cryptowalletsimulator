package com.cryptowallet.simulator.exception;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String uuid) {
        super("Could not find wallet with uuid = " + uuid);
    }
}
