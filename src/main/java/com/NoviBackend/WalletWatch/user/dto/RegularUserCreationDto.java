package com.NoviBackend.WalletWatch.user.dto;

import jakarta.validation.constraints.Email;

public class RegularUserCreationDto {
    private String username;
    private String password;
    private String firstName;
    private String surname;

    @Email
    private String emailAddress;

    public RegularUserCreationDto() {
    }

    public RegularUserCreationDto(String username, String password, String firstName, String surname, String emailAddress) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String email) {
        this.emailAddress = email;
    }
}
