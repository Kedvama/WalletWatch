package com.NoviBackend.WalletWatch.user.professional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfUserRepository extends JpaRepository<ProfessionalUser, Long> {
    boolean existsProfessionalUserByUsername(String username);
    boolean existsProfessionalUserByEmailAddress(String emailAddress);
    Optional<ProfessionalUser> findProfessionalUserByUsername(String username);
}
