package com.NoviBackend.WalletWatch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class springsecurityconfig {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws
            Exception{

    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http)throws Exception{

    }
}
