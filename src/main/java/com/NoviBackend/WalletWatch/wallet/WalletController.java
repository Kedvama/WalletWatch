package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import com.NoviBackend.WalletWatch.stock.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<WalletDto> getAllPublicWallets(){
        List<WalletDto> walletList = walletService.getAllPublicWallets();
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
}


