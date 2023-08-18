package com.NoviBackend.WalletWatch.repository;

import com.NoviBackend.WalletWatch.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository <Wallet, Long> {
    List<Wallet> findWalletBySharedIsTrue();
    Optional<Wallet> findWalletBySharedIsTrueAndId(long id);
}