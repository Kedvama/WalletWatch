package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.wallet.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity(name = "Stocks")
public class Stock {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stockName;

    @Column
    private String type;

    @Column
    private int value;

    @Column
    private int buyLimit;

    @Column
    private int sellLimit;

    @Column
    private String notations;

    @Column
    private float percentageGoal;

    @ManyToOne
    @JoinColumn(name="wallet_id", nullable = false)
    @JsonIgnore
    private Wallet wallet;

    // Constructor
    public Stock(){}

    public Stock(String stockName, String type, int value, int buyLimit, int sellLimit, String notations, float percentageGoal) {
        this.stockName = stockName;
        this.type = type;
        this.value = value;
        this.buyLimit = buyLimit;
        this.sellLimit = sellLimit;
        this.notations = notations;
        this.percentageGoal = percentageGoal;
    }

    // Getters & Setters
    public Long getId(){
        return id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(int buyLimit) {
        this.buyLimit = buyLimit;
    }

    public int getSellLimit() {
        return sellLimit;
    }

    public void setSellLimit(int sellLimit) {
        this.sellLimit = sellLimit;
    }

    public String getNotations() {
        return notations;
    }

    public void setNotations(String notations) {
        this.notations = notations;
    }

    public float getPercentageGoal() {
        return percentageGoal;
    }

    public void setPercentageGoal(float percentageGoal) {
        this.percentageGoal = percentageGoal;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", stockName='" + stockName + '\'' +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", buyLimit=" + buyLimit +
                ", sellLimit=" + sellLimit +
                ", notations='" + notations + '\'' +
                ", percentageGoal=" + percentageGoal +
                '}';
    }
}
