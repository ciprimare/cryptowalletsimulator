package com.cryptowallet.simulator.model.wallet;

import javax.validation.Valid;

public class WalletEntryTransaction {
    private @Valid WalletEntry from;
    private @Valid WalletEntry to;

    public WalletEntry getFrom() {
        return from;
    }

    public void setFrom(WalletEntry from) {
        this.from = from;
    }

    public WalletEntry getTo() {
        return to;
    }

    public void setTo(WalletEntry to) {
        this.to = to;
    }
}
