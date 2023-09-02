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
    private BigDecimal value;

    @Column
    private Long amount;

    @Column
    private BigDecimal buyLimit;

    @Column
    private BigDecimal sellLimit;

    @Column
    private String notations;

    @Column
    private BigDecimal percentageGoal;

    @ManyToOne
    @JoinColumn(name="wallet_id", nullable = false)
    @JsonIgnore
    private Wallet wallet;

    // Constructor
    public Stock(){}

    public Stock(String stockName, BigDecimal value,
                 BigDecimal buyLimit, BigDecimal sellLimit,
                 String notations, BigDecimal percentageGoal) {
        this.stockName = stockName;
        this.value = value;
        this.buyLimit = buyLimit;
        this.sellLimit = sellLimit;
        this.notations = notations;
        this.percentageGoal = percentageGoal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

    public BigDecimal getPercentageGoal() {
        return percentageGoal;
    }

    public void setPercentageGoal(BigDecimal percentageGoal) {
        this.percentageGoal = percentageGoal;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
