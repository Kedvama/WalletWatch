package com.NoviBackend.WalletWatch.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    public AuthController(JwtUtil jwtUtil, DataSource dataSource) {
        this.jwtUtil = jwtUtil;
        this.jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    }

    @PostMapping("/requesttoken")
    public String login(@RequestBody @Validated LoginRequest request){
        /*
        creeer een securityservice die de request pakt en kijkt of de inlog geldig is.
        Zoja, stuur JwtUtil generate token terug.
         */

        return jwtUtil.generateToken(jdbcUserDetailsManager.loadUserByUsername(request.getUsername()));
    }

    @GetMapping("/test")
    public String testing(Authentication authentication){
        return authentication.getName();
    }
}


/*
TODO

- how get user from the security.context.
 */