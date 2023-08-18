package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionRepository extends JpaRepository<Stock, Long> {
}
