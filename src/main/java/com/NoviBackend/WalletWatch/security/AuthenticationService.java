package com.NoviBackend.WalletWatch.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import com.NoviBackend.WalletWatch.security.mapper.SecurityUserMapper;
import com.NoviBackend.WalletWatch.user.dto.RegularUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

    public SecurityUser findByUsername(String username){
        Optional<SecurityUser> user = securityUserRepository.findSecurityUserByUsername(username);
        return user.orElse(null);
    }

    public boolean checkCredentials(LoginRequest request) {
        boolean matches = false;
        SecurityUser user = findByUsername(request.getUsername());

        if(user != null){
            matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        }
        return matches;
    }

    public void saveRegularUser(RegularUserDto userDto) {
        SecurityUser user = new SecurityUser();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Authority auth = new Authority(user.getUsername(), "ROLE_USER");

        user.addAuthorities(auth);
        securityUserRepository.save(user);
        authoritiesRepository.save(auth);
    }
}
