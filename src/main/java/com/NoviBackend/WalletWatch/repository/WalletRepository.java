package com.NoviBackend.WalletWatch.repository;

import com.NoviBackend.WalletWatch.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository <Wallet, Long> {
}
