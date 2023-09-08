package com.NoviBackend.WalletWatch.subscription.dto;

import com.NoviBackend.WalletWatch.stock.Stock;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionCompareDto {
    private List<Stock> buy = new ArrayList<>();
    private List<Stock> sell = new ArrayList<>();
    private List<Stock> hold = new ArrayList<>();
    private List<Stock> other = new ArrayList<>();

    public SubscriptionCompareDto() {
    }

    public SubscriptionCompareDto(List<Stock> buy, List<Stock> sell, List<Stock> hold, List<Stock> other) {
        this.buy = buy;
        this.sell = sell;
        this.hold = hold;
        this.other = other;
    }

    public List<Stock> getBuy() {
        return buy;
    }

    public void setBuy(List<Stock> buy) {
        this.buy = buy;
    }

    public List<Stock> getSell() {
        return sell;
    }

    public void setSell(List<Stock> sell) {
        this.sell = sell;
    }

    public List<Stock> getHold() {
        return hold;
    }

    public void setHold(List<Stock> hold) {
        this.hold = hold;
    }

    public List<Stock> getOther() {
        return other;
    }

    public void setOther(List<Stock> other) {
        this.other = other;
    }

    public void addBuy(Stock stock) {
        this.buy.add(stock);
    }

    public void addSell(Stock stock){
        this.sell.add(stock);
    }

    public void addHold(Stock stock){
        this.hold.add(stock);
    }

    public void addOther(Stock stock){
        this.other.add(stock);
    }
}
