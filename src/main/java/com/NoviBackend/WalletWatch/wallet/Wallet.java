package com.NoviBackend.WalletWatch.wallet;

import jakarta.persistence.*;

@Entity(name = "Wallets")
public class Wallet {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    Boolean shared = false;

    // Constructor
    public Wallet() {
    }

    // Getters
    public Long getId(){
        return id;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }
}
