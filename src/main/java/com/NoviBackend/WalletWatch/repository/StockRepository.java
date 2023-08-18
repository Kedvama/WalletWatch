package com.NoviBackend.WalletWatch.repository;

import com.NoviBackend.WalletWatch.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
