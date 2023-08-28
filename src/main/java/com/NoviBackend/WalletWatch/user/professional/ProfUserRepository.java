package com.NoviBackend.WalletWatch.user.professional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfUserRepository extends JpaRepository<ProfessionalUser, Long> {
    boolean existsProfessionalUserByUsername(String username);
    boolean existsProfessionalUserByEmailAddress(String emailAddress);
}
