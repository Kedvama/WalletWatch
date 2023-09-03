package com.NoviBackend.WalletWatch.stock;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.stock.stockdto.StockDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stocks")
    public List<Stock> getYourStocks(Authentication auth){
        List<Stock> stocks = stockService.getYourStocks(auth.getName(), auth.getAuthorities());

        if(stocks == null){
            throw new EntityNotFoundException("No shared stocks found.");
        }

        return stocks;
    }

    @GetMapping("/stocks/{id}")
    public Stock getStock(@PathVariable Long id, Authentication auth){
        Stock stock = stockService.getStock(auth.getName(), auth.getAuthorities(), id);

        if(stock == null){
            throw new EntityNotFoundException("Stock in wallet with id: " + id + ", not found");
        }

        return stock;
    }

    @PostMapping("/stocks")
    public ResponseEntity<Object> addStockToWallet(@RequestBody StockDto stockDto, Authentication auth){
        Long stockId = stockService.addStock(auth.getName(), auth.getAuthorities(), stockDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/stocks/{id}")
                .buildAndExpand(stockId).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/stocks")
    public ResponseEntity<Object> deleteStock(@RequestBody Stock stock){
        // delete stock if in your wallet.

        return ResponseEntity.noContent().build();
    }

}
