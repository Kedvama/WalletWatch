package com.NoviBackend.WalletWatch.wallet.dto;

import com.NoviBackend.WalletWatch.stock.Stock;

import java.util.List;

public class RegularPersonalWalletDto {
    private Long id;
    private Long amount;
    private Long value;
    private List<Stock> stocks;

    public RegularPersonalWalletDto() {
    }

    public RegularPersonalWalletDto(Long id, List<Stock> stocks) {
        this.id = id;
        this.stocks = stocks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
