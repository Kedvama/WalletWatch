package com.NoviBackend.WalletWatch.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
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
        try{
            if(user != null) {
                Optional<Authority> optionalAuth = user.getAuthorities().stream()
                        .filter(auth -> auth.getAuthority().equals(oldRole))
                        .findFirst();

                // change the authority
                if (optionalAuth.isPresent()) {
                    Authority auth = optionalAuth.get();
                    auth.setAuthority(newRole);
                    securityUserRepository.save(user);
                }
            }
        }catch (NullPointerException ex){
               throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }
    }

    public boolean checkCredentials(LoginRequest request) {
        boolean matches = false;
        SecurityUser user = findByUsername(request.getUsername());

        if(user != null & request.getPassword() != null){
            matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        }
        return matches;
    }

    public void saveRegularUser(RegularUserCreationDto userDto) {
        Authority auth = new Authority(userDto.getUsername(), "ROLE_USER");

        SecurityUser user = createSecurityUser(userDto, auth);

        securityUserRepository.save(user);
        authoritiesRepository.save(auth);
    }

    private SecurityUser findByUsername(String username){
        Optional<SecurityUser> user = securityUserRepository.findSecurityUserByUsername(username);
        return user.orElse(null);
    }

    private SecurityUser createSecurityUser(RegularUserCreationDto userDto,
                                            Authority auth){
        SecurityUser user = new SecurityUser();

        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAuthorities(Arrays.asList(auth));

        return user;
    }
}
