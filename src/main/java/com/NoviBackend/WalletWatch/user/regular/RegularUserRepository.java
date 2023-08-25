package com.NoviBackend.WalletWatch.user.regular;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularUserRepository extends JpaRepository<RegularUser, Long> {
    boolean existsRegularUserByUsername(String username);
    boolean existsRegularUserByEmailAddress(String emailAddress);
}
