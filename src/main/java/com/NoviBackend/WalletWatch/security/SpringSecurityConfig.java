package com.NoviBackend.WalletWatch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig {

    private final DataSource dataSource;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(DataSource dataSource, JwtRequestFilter jwtRequestFilter){
        this.dataSource = dataSource;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled" +
                        " FROM users" +
                        " WHERE username=?")
                .authoritiesByUsernameQuery("SELECT username, authority" +
                        " FROM authorities " +
                        " WHERE username=?");
        return authenticationManagerBuilder.build();
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception{
        //noinspection removal
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.DELETE, "/stocks/{id}").hasAnyRole("ADMIN", "PROF", "USER")
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/profs/**").hasAnyRole("ADMIN", "PROF", "USER")
                .requestMatchers(HttpMethod.GET, "/prof/**").hasRole("PROF")
                .requestMatchers(HttpMethod.GET, "/subscriptions").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/subscriptions/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/subscriptions").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/user/subscriptions/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/stocks").hasAnyRole("ADMIN", "PROF", "USER")
                .requestMatchers(HttpMethod.GET, "/stocks/{id}").hasAnyRole("ADMIN", "PROF", "USER")
                .requestMatchers(HttpMethod.GET, "/wallets/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/wallet").hasRole("PROF")
                .requestMatchers(HttpMethod.POST, "/stocks").hasAnyRole("ADMIN", "PROF", "USER")
                .requestMatchers(HttpMethod.POST, "/subscriptions").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                .requestMatchers(HttpMethod.POST,"/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/promote").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/prof/demote").hasRole("PROF")
                .requestMatchers(HttpMethod.PUT, "/stocks/{id}").hasAnyRole("ADMIN", "PROF", "USER")
                .requestMatchers(HttpMethod.PUT, "/subscriptions").hasAnyRole("ADMIN", "USER")

                .anyRequest().denyAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
