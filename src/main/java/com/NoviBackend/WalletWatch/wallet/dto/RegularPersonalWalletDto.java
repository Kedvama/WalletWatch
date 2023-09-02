package com.NoviBackend.WalletWatch.wallet.dto;

import com.NoviBackend.WalletWatch.stock.Stock;

import java.math.BigDecimal;
import java.util.List;

public class RegularPersonalWalletDto {
    private Long id;
    private Long amount;
    private BigDecimal value;
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setValue(List<Stock> stocks){
        if(stocks.size() == 0){
            this.value = BigDecimal.valueOf(0);

        }else{
            BigDecimal totalValue = new BigDecimal(0);

            for(Stock stock: stocks){
                totalValue  =  totalValue
                        .add(stock.getValue()
                        .multiply(new BigDecimal(
                                stock.getAmount())));
            }
            this.value = totalValue;
        }
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;

        // also set amout of stocks
        setAmount((long) stocks.size());

        // and set value of wallet
        setValue(stocks);
    }
}
