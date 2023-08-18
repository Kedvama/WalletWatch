package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.stock.Stock;
import jakarta.persistence.*;

import java.util.Set;

@Entity(name = "Wallets")
public class Wallet {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean shared = false;

    // @manytoone -> to user
    // private String username;
    // make user dto to show username. mabey make it possible to see email only when subscribed.

    @OneToMany(mappedBy = "wallet")
    private Set<Stock> stocks;

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

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }
}
