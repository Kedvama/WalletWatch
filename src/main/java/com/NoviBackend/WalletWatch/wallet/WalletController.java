package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import com.NoviBackend.WalletWatch.stock.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService, StockService stockService){
        this.walletService = walletService;
    }

    @GetMapping("/wallets")
    public List<WalletDto> getAllPublicWallets(){
        List<WalletDto> walletList = walletService.getAllPublicWallets();
        if(walletList == null)
            throw new EntityNotFoundException("No shared wallets found.");

        return walletList;
    }

    @GetMapping("/wallets/{id}")
    public Wallet getPublicWalletById(@PathVariable int id){
        Wallet wallet = walletService.findPublicById(id);
        if(wallet == null)
            throw new EntityNotFoundException("Shared wallet id: " + id + ", not found.");

        return wallet;
    }
}


