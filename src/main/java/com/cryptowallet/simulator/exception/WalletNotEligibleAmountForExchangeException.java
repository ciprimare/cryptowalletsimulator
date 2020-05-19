package com.cryptowallet.simulator.exception;

import java.math.BigDecimal;

public class WalletNotEligibleAmountForExchangeException extends RuntimeException {
    public WalletNotEligibleAmountForExchangeException(String walletId, BigDecimal requestedAmount, BigDecimal eligibleAmount) {
        super("Not enough currency amount in the wallet [" + walletId + "]. Request/Available [" + requestedAmount + "]/[" + eligibleAmount + "]");
    }
}
