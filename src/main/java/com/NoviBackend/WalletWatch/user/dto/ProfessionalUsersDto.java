package com.NoviBackend.WalletWatch.user.dto;

public class ProfessionalUsersDto {
    private String username;
    private String company;
    private String introduction;

    public ProfessionalUsersDto() {
    }

    public ProfessionalUsersDto(String username, String company, String introduction) {
        this.username = username;
        this.company = company;
        this.introduction = introduction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
