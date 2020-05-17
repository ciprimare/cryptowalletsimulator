package com.cryptowallet.simulator.model.wallet;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Wallet {
    private UUID id;
    private String name;
    private Set<WalletEntry> entries;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WalletEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<WalletEntry> entries) {
        this.entries = entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return Objects.equals(name, wallet.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", entries=" + entries +
                '}';
    }
}
