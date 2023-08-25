package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.stock.Stock;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Wallets")
public class Wallet {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean shared = false;

    @OneToMany(mappedBy = "wallet")
    private List<Stock> stocks;


    // Constructor
    public Wallet() {
        this.stocks = new ArrayList<>();
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

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", shared=" + shared +
                ", stocks=" + stocks +
                '}';
    }
}

// could add:
// - amount stocks
// - amount followers
// and more to the Wallet or WalletDto, so people could get some more info
// about the wallet before they subscribe.
