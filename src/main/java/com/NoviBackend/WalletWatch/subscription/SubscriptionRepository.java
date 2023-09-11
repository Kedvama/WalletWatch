package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    int countByProfessionalUser(ProfessionalUser prof);
    void deleteAllByProfessionalUser(ProfessionalUser professionalUser);
}
