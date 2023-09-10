package com.NoviBackend.WalletWatch.stock.stockdto;

import java.math.BigDecimal;

public class StockDto {
    private String stockName;
    private BigDecimal value;
    private Long quantity;
    private BigDecimal buyLimit;
    private BigDecimal sellLimit;
    private String notations;
    private BigDecimal percentageGoal;
    private String action;

    public StockDto() {
    }

    public StockDto(String stockName, BigDecimal value, Long quantity, BigDecimal buyLimit, BigDecimal sellLimit, String notations, BigDecimal percentageGoal, String action) {
        this.stockName = stockName;
        this.value = value;
        this.quantity = quantity;
        this.buyLimit = buyLimit;
        this.sellLimit = sellLimit;
        this.notations = notations;
        this.percentageGoal = percentageGoal;
        this.action = action;
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
