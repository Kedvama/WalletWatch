package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.wallet.Wallet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

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
    private BigDecimal value;

    @Column
    private BigDecimal buyLimit;

    @Column
    private BigDecimal sellLimit;

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

    public Stock(String type, BigDecimal value, BigDecimal buyLimit, BigDecimal sellLimit, String notations, float percentageGoal) {
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(BigDecimal buyLimit) {
        this.buyLimit = buyLimit;
    }

    public BigDecimal getSellLimit() {
        return sellLimit;
    }

    public void setSellLimit(BigDecimal sellLimit) {
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

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", buyLimit=" + buyLimit +
                ", sellLimit=" + sellLimit +
                ", notations='" + notations + '\'' +
                ", percentageGoal=" + percentageGoal +
                '}';
    }
}
