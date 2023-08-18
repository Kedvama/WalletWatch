package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.repository.SubscriptionRepository;
import com.NoviBackend.WalletWatch.repository.WalletRepository;
import com.NoviBackend.WalletWatch.stock.Stock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private WalletRepository walletRepository;
    private SubscriptionRepository subscriptionRepository;

    public WalletService(WalletRepository walletRepository, SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository = subscriptionRepository;
        this.walletRepository = walletRepository;
    }

    public List<Wallet> getAllPublicWallets() {
        List<Wallet> walletList = walletRepository.findWalletBySharedIsTrue();
        if(walletList.isEmpty())
            return null;

        return walletList;
    }

    public Wallet findPublicById(int id) {
        Optional<Wallet> wallet = walletRepository.findWalletBySharedIsTrueAndId((long) id);
        if(wallet.isEmpty())
            return null;

        return wallet.get();
    }

    public Long addStock(Stock stock) {
        return null;
    }
}
