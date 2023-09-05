package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.stock.Stock;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    int countByProfessionalUser(ProfessionalUser prof);
    void deleteAllByProfessionalUser(ProfessionalUser professionalUser);
}
