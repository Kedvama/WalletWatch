package com.NoviBackend.WalletWatch.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, String> {
    Optional<SecurityUser> findSecurityUserByUsername(String username);
}
