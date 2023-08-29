package com.NoviBackend.WalletWatch.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final SecurityUserRepository securityUserRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder, SecurityUserRepository securityUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.securityUserRepository = securityUserRepository;
    }

    public SecurityUser findByUsername(String username){
        Optional<SecurityUser> user = securityUserRepository.findSecurityUserByUsername(username);
        return user.orElse(null);
    }

    public boolean checkCredentials(LoginRequest request) {
        boolean isUser = false;

        SecurityUser user = findByUsername(request.getUsername());
        String requestPasswordHashed = passwordEncoder.encode(request.getPassword());

        if(user != null && user.getPassword().equals(requestPasswordHashed)){
            isUser = true;
        }

        return isUser;
    }
}
