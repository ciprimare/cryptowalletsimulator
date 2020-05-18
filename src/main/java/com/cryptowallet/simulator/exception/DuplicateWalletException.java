package com.cryptowallet.simulator.exception;

public class DuplicateWalletException extends RuntimeException {
    public DuplicateWalletException(String name) {
        super("Wallet with name = [" + name + "] already exist! Please choose a different name");
    }
}
