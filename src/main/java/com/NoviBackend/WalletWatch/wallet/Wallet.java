package com.NoviBackend.WalletWatch.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "Wallets")
public class Wallet {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constructor
    public Wallet() {
    }

    // Getters
    public Long getId(){
        return id;
    }
}
