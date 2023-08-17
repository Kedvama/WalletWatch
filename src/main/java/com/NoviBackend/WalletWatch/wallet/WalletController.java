package com.NoviBackend.WalletWatch.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService){
        this. walletService = walletService;
    }

    @GetMapping("/wallets")
    public List<Wallet> getAllSharedWallets(){
        List<Wallet> walletList = walletService.getAllSharedWallets();

        return walletList;
    }

    @GetMapping("/wallets/{id}")
    public Wallet getWalletById(@PathVariable int id){
        Wallet wallet = walletService.findById(id);
        if(wallet == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return wallet;
    }
}
