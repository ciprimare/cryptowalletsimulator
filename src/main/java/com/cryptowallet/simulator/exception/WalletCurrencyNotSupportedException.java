package com.cryptowallet.simulator.exception;

public class WalletCurrencyNotSupportedException extends RuntimeException {
    public WalletCurrencyNotSupportedException(String notSupportedCurrencies) {
        super("The wallet does not support the following currencies = " + notSupportedCurrencies + " already exist! Please choose a different name");
    }
}
