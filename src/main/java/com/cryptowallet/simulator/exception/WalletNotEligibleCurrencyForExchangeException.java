package com.cryptowallet.simulator.exception;

public class WalletNotEligibleCurrencyForExchangeException extends RuntimeException {

    public WalletNotEligibleCurrencyForExchangeException(String walletId, String notEligibleCurrency) {
        super("This currency [" + notEligibleCurrency +"] is not eligible for exchange from wallet: " + walletId);
    }
}
