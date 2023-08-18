package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public Stock findById(int id) {
        Optional<Stock> stock = stockRepository.findById((long) id);
        if(stock.isEmpty())
            return null;

        return stock.get();
    }
}
