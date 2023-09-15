package com.NoviBackend.WalletWatch.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegularUserCreationDto {
    @NotBlank(message = "username is required")
    private String username;

    @NotNull(message = "password required")
    @Size(min=8, message = "password must be greater then 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Please enter surname")
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
