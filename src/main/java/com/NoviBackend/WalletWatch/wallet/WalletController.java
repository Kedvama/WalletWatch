package com.NoviBackend.WalletWatch.wallet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
