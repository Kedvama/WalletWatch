package com.NoviBackend.WalletWatch.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final SecurityUserRepository securityUserRepository;
    private final AuthoritiesRepository authoritiesRepository;


    public AuthenticationService(PasswordEncoder passwordEncoder,
                                 SecurityUserRepository securityUserRepository,
                                 AuthoritiesRepository authoritiesRepository) {
        this.passwordEncoder = passwordEncoder;
        this.securityUserRepository = securityUserRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    public void changeRole(String username, String newRole, String  oldRole){
        // get user
        SecurityUser user = findByUsername(username);

        // find the authority
        if(user != null){
            Optional<Authority> optionalAuth = user.getAuthorities().stream()
                    .filter(auth -> auth.getAuthority().equals(oldRole))
                    .findFirst();

            // change the authority
            if(optionalAuth.isPresent()){
                Authority auth = optionalAuth.get();
                auth.setAuthority(newRole);
                securityUserRepository.save(user);
            }
        }
    }

    public boolean checkCredentials(LoginRequest request) {
        boolean matches = false;
        SecurityUser user = findByUsername(request.getUsername());

        if(user != null){
            matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        }
        return matches;
    }

    public SecurityUser findByUsername(String username){
        Optional<SecurityUser> user = securityUserRepository.findSecurityUserByUsername(username);
        return user.orElse(null);
    }

    public void saveRegularUser(RegularUserCreationDto userDto) {
        SecurityUser user = new SecurityUser();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Authority auth = new Authority(user.getUsername(), "ROLE_USER");

        user.addAuthorities(auth);
        securityUserRepository.save(user);
        authoritiesRepository.save(auth);
    }
}
