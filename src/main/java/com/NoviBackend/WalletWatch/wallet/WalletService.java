package com.NoviBackend.WalletWatch.wallet;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository){
        this.walletRepository = walletRepository;
    }

    public List<Wallet> getAllSharedWallets() {
        return walletRepository.findAll();
    }
}
