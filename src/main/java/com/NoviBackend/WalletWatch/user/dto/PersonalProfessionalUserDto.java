package com.NoviBackend.WalletWatch.user.dto;

public class PersonalProfessionalUserDto {
    private String username;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String company;
    private String introduction;
    private int subscriptionsQuantity;

    public PersonalProfessionalUserDto() {
    }

    public PersonalProfessionalUserDto(String username, String firstName, String surname, String emailAddress, String company, String introduction, int subscriptionsQuantity) {
        this.username = username;
        this.firstName = firstName;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.company = company;
        this.introduction = introduction;
        this.subscriptionsQuantity = subscriptionsQuantity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getSubscriptionsQuantity() {
        return subscriptionsQuantity;
    }

    public void setSubscriptionsQuantity(int subscriptionsQuantity) {
        this.subscriptionsQuantity = subscriptionsQuantity;
    }
}
