package com.cryptowallet.simulator.model.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class WalletEntry {
    @JsonIgnore
    private UUID walletId;
    @NotEmpty(message = "currency wallet must not be null or empty")
    private String currency;
    @Min(value = 0, message = "wallet amount should be positive")
    private BigDecimal amount;

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WalletEntry that = (WalletEntry) o;

        if (!Objects.equals(walletId, that.walletId)) return false;
        return Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        int result = walletId != null ? walletId.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WalletEntry{" +
                "walletId=" + walletId +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
