package com.cryptowallet.simulator.exception;

public class ExchangeRateNotFoundException extends RuntimeException {
    public ExchangeRateNotFoundException(String fsym, String tsym) {
        super("Exchange rate could not be found for between: " + fsym + " and " + tsym);
    }
}
