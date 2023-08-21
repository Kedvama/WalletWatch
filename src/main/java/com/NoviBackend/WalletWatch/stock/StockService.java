package com.NoviBackend.WalletWatch.stock;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public Stock findById(int id) {
        // find the stock, this will need another check to see if the stock is inside your wallet.
        // else it should not be shown.
        Optional<Stock> stock = stockRepository.findById((long) id);
        return stock.orElse(null);
    }

    public void addStock(Stock stock){
        stockRepository.save(stock);
    }
}
