package com.NoviBackend.WalletWatch.user.regular;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegularUserRepository extends JpaRepository<RegularUser, Long> {
    boolean existsRegularUserByUsername(String username);
    boolean existsRegularUserByEmailAddress(String emailAddress);
    Optional<RegularUser> findRegularUserByUsername(String username);
}
