package com.NoviBackend.WalletWatch.security;

import com.NoviBackend.WalletWatch.exception.InvalidLoginCredentials;
import com.NoviBackend.WalletWatch.request.LoginRequest;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final AuthenticationService authenticationService;

    public AuthController(DataSource dataSource, AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/auth")
    public String login(@RequestBody @Validated LoginRequest request){

        if(!authenticationService.checkCredentials(request)){
            throw new InvalidLoginCredentials("Invalid login credentials");
        }

        return jwtUtil.generateToken(jdbcUserDetailsManager.loadUserByUsername(request.getUsername()));
    }
}