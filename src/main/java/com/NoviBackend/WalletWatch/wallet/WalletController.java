package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.stock.Stock;
import com.NoviBackend.WalletWatch.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class WalletController {

    private final WalletService walletService;
    private final StockService stockService;

    public WalletController(WalletService walletService, StockService stockService){
        this.walletService = walletService;
        this.stockService =stockService;
    }

    @GetMapping("/wallets")
    public List<Wallet> getAllPublicWallets(){
        List<Wallet> walletList = walletService.getAllPublicWallets();
        if(walletList == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return walletList;
    }

    @GetMapping("/wallets/{id}")
    public Wallet getPublicWalletById(@PathVariable int id){
        Wallet wallet = walletService.findPublicById(id);
        if(wallet == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return wallet;
    }

    @GetMapping("/yourwallet")
    public Wallet getYourStocks(){
        Wallet wallet = walletService.findPublicById(6); // this is to test if it works, this will return your own stock
        if(wallet == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return wallet;
    }

    @PostMapping("/yourwallet")
    public ResponseEntity<Object> add(@RequestBody Stock stock){
        Long stockId = walletService.addStock(stock);

        URI location =  ServletUriComponentsBuilder.fromCurrentRequest().path("/{stockId}")
                .buildAndExpand(stockId).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/yourwallet/{id}")
    // this play needs to be edited with a findByIdAndWalletId so you will only get the
    // stock back if it is inside your own wallet.
    public Stock getAStock(@PathVariable int id){
        Stock stock = stockService.findById(id);
        if(stock == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return stock;
    }
}
