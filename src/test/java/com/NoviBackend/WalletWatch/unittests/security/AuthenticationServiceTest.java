package com.NoviBackend.WalletWatch.unittests.security;

import com.NoviBackend.WalletWatch.request.LoginRequest;
import com.NoviBackend.WalletWatch.security.*;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @MockBean
    private AuthoritiesRepository authoritiesRepository;

    @Captor
    private ArgumentCaptor<SecurityUser> securityUserArgumentCaptor;

    @Captor
    private ArgumentCaptor<Authority> authorityArgumentCaptor;

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
    void changeRole_UserNull(){
        // act
        authService.changeRole("NotWouter", "ROLE_ADMIN", "ROLE_USER");

        // arrange -> securityUserRepository must not be called
        Mockito.verify(securityUserRepository, Mockito.times(0)).save(any(SecurityUser.class));
    }

    @Test
    void changeRole_UserNotNull_AuthNotNull(){
        // arrange
        authority = new Authority();
        authority.setUsername("wouter");
        user.addAuthorities(authority);

        Mockito.when(securityUserRepository.findSecurityUserByUsername("wouter"))
                .thenReturn(Optional.ofNullable(user));

        // act & assert
        Exception exception = assertThrows(ResponseStatusException.class, () ->
                authService.changeRole("wouter", "ROLE_ADMIN", null));

        assertEquals("500 INTERNAL_SERVER_ERROR", exception.getMessage());
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
    void saveRegularUser_SecurityUserTest() {
        // arrange
        RegularUserCreationDto userDto = new RegularUserCreationDto("wouter2",
                "password",
                "wouter",
                "novi",
                "email@gmail.com");


        // act
        authService.saveRegularUser(userDto);

        // Verify that the save method was called with the correct user
        verify(securityUserRepository).save(securityUserArgumentCaptor.capture());
        SecurityUser savedUser = securityUserArgumentCaptor.getValue();

        // assert
        Mockito.verify(securityUserRepository, Mockito.times(1)).save(any(SecurityUser.class));
        assertEquals(userDto.getUsername(), savedUser.getUsername());
    }

    @Test
    void saveRegularUser_AuthoritiesTest(){
        // arrange
        RegularUserCreationDto userDto = new RegularUserCreationDto("wouter2",
                "password",
                "wouter",
                "novi",
                "email@gmail.com");

        // act
        authService.saveRegularUser(userDto);

        // Verify that the save method was called with the correct authorities
        verify(authoritiesRepository).save(authorityArgumentCaptor.capture());
        Authority localAuthority = authorityArgumentCaptor.getValue();

        // assert
        Mockito.verify(authoritiesRepository, Mockito.times(1)).save(any(Authority.class));
        assertEquals(userDto.getUsername(), localAuthority.getUsername());
        assertEquals("ROLE_USER", localAuthority.getAuthority());
    }

    @Test
    void savedRegularUser_PasswordCheck(){
        // arrange
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RegularUserCreationDto userDto = new RegularUserCreationDto("wouter2",
                "password",
                "wouter",
                "novi",
                "email@gmail.com");

        // act
        authService.saveRegularUser(userDto);
        verify(securityUserRepository).save(securityUserArgumentCaptor.capture());
        SecurityUser savedUser = securityUserArgumentCaptor.getValue();

        // assert
        assertTrue(passwordEncoder.matches(userDto.getPassword(), savedUser.getPassword()));
    }
}