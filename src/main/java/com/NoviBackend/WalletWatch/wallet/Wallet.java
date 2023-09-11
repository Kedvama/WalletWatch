package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.stock.Stock;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Wallets")
public class Wallet {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Boolean shared = false;

    @OneToMany(mappedBy = "wallet")
    private List<Stock> stocks;


    // Constructor
    public Wallet() {
        this.stocks = new ArrayList<>();
    }

    // Getters & Setters
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


    // methods
    public void setStock(Stock oldStock, Stock newStock){
        int stockIndex = this.stocks.indexOf(oldStock);
        this.stocks.set(stockIndex, newStock);
    }

    public void addStock(Stock stock){
        this.stocks.add(stock);
    }

    public boolean deleteStock(Stock stock) {
        return this.stocks.remove(stock);
    }
}

