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

    @PostMapping("/stocks")
    public ResponseEntity<Object> addStockToWallet(@RequestBody StockDto stockDto, Authentication auth){
        Long stockId = stockService.addStock(auth.getName(), auth.getAuthorities(), stockDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/stocks/{id}")
                .buildAndExpand(stockId).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/stocks/{id}")
    public Stock getStock(@PathVariable Long id, Authentication auth){
        Stock stock = stockService.getStock(auth.getName(), auth.getAuthorities(), id);

        if(stock == null){
            throw new EntityNotFoundException("Stock in wallet with id: " + id + ", not found");
        }

        return stock;
    }

    @PutMapping("/stocks/{id}")
    public ResponseEntity<Object> updateStock(@RequestBody StockDto stockDto,
                                              @PathVariable Long id,
                                              Authentication auth){
        Long replacedStockId = stockService.updateStock(id, stockDto, auth);

        if(replacedStockId == null){
            throw new EntityNotFoundException("Stock with id: " + id + ", not found.");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/stocks/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Object> deleteStockFromWallet(@PathVariable Long id,
                                                        Authentication auth){

        Long removedId  = stockService.deleteStock(id, auth);

        if(removedId == null){
            throw new EntityNotFoundException("Could not remove stock with id : " + id);
        }

        return ResponseEntity.noContent().build();
    }
}
