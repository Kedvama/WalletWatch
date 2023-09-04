package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.stock.Stock;
import com.NoviBackend.WalletWatch.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    int countByWallet(Wallet wallet);
}
