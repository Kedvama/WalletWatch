package com.NoviBackend.WalletWatch.unittests.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import com.NoviBackend.WalletWatch.security.*;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authService;

    @MockBean
    private SecurityUserRepository securityUserRepository;

    @Mock
    Authority authority;

    @Mock
    SecurityUser user;

    @BeforeEach
    void setup(){
        // arrange
        user = new SecurityUser();
        user.setUsername("wouter");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode("password"));
    }

    @Test
    void changeRole_ToAdmin() {
        // Arrange
        authority = new Authority("wouter", "ROLE_USER");
        user.addAuthorities(authority);

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act
        authService.changeRole("wouter", "ROLE_ADMIN", "ROLE_USER");

        // assert
        assertEquals("ROLE_ADMIN", securityUserRepository
                                                .findSecurityUserByUsername("wouter")
                                                .get().getAuthorities()
                                                .get(0).getAuthority());
    }

    @Test
    void changeRole_WrongAuth(){
        // arrange
        authority = new Authority("wouter", "ROLE_PROF");
        user.addAuthorities(authority);

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act
        authService.changeRole("wouter", "ROLE_ADMIN", "TEST_ROLE");

        // assert
        assertEquals("ROLE_PROF", securityUserRepository
                .findSecurityUserByUsername("wouter")
                .get().getAuthorities()
                .get(0).getAuthority());
    }

    @Test
    void changeRole_UserNotNull_AuthNotNull(){
        // arrange
        authority = new Authority();
        authority.setUsername("testname");
        user.addAuthorities(authority);

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act & assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authService.changeRole("wouter", "ROLE_ADMIN", null));
    }

    @Test
    void checkCredentials_Correct_UsernamePassword() {
        // arrange
        LoginRequest request = new LoginRequest("wouter", "password");

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act
        boolean canLogin = authService.checkCredentials(request);

        // assert
        assertEquals(true, canLogin);
    }

    @Test
    void checkCredentials_inCorrect_Password() {
        // arrange
        LoginRequest request = new LoginRequest("wouter", "not the password");

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act
        boolean canLogin = authService.checkCredentials(request);

        // assert
        assertEquals(false, canLogin);
    }

    @Test
    void checkCredentials_NoPassword() {
        // arrange
        LoginRequest request = new LoginRequest("wouter", null);

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act
        boolean canLogin = authService.checkCredentials(request);

        // assert
        assertEquals(false, canLogin);
    }

    @Test
    void saveRegularUser() {
        // arrange
        RegularUserCreationDto user = new RegularUserCreationDto("wouter", "password", "", "", "");

        when(securityUserRepository.save(Mockito.any(SecurityUser.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        // act

    }

    @Test
    void findByUsername() {
    }
}