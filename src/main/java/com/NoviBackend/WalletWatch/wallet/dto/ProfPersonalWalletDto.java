package com.NoviBackend.WalletWatch.wallet.dto;


import com.NoviBackend.WalletWatch.stock.Stock;

import java.math.BigDecimal;
import java.util.List;

public class ProfPersonalWalletDto {
    private Long id;
    private Long quantity;
    private BigDecimal value;
    private Boolean shared;
    private List<Stock> stocks;

    public ProfPersonalWalletDto() {
    }

    public ProfPersonalWalletDto(Long id, List<Stock> stocks, boolean shared) {
        this.id = id;
        this.stocks = stocks;
        this.shared = shared;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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
                                        stock.getQuantity())));
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
        setQuantity((long) stocks.size());

        // and set value of wallet
        setValue(stocks);
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }
}
