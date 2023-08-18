package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.dto.WalletDto;
import com.NoviBackend.WalletWatch.mapper.WalletMapper;
import com.NoviBackend.WalletWatch.repository.SubscriptionRepository;
import com.NoviBackend.WalletWatch.repository.WalletRepository;
import com.NoviBackend.WalletWatch.stock.Stock;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WalletMapper walletMapper;

    public WalletService(WalletRepository walletRepository,
                         SubscriptionRepository subscriptionRepository,
                         WalletMapper walletMapper){
        this.subscriptionRepository = subscriptionRepository;
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    public List<WalletDto> getAllPublicWallets() {
        List<Wallet> walletList = walletRepository.findWalletBySharedIsTrue();
        if(walletList.isEmpty())
            return null;


        return walletMapper.convertWalletToDtoList(walletList);
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
