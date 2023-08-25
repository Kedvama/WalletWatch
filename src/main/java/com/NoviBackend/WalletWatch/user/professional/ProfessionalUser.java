package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.user.AbstractUsers;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ProfessionalUser extends AbstractUsers {

    @Column
    private String company;

    @Column
    private String introduction;

    public ProfessionalUser(){

    }

    public ProfessionalUser(String username, String firstName,
                            String surname, String emailAddress,
                            String company, String shortIntroduction) {

        super(username, firstName, surname, emailAddress);
        this.company = company;
        this.introduction = shortIntroduction;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getShortIntroduction() {
        return introduction;
    }

    public void setShortIntroduction(String shortIntroduction) {
        this.introduction = shortIntroduction;
    }

    public Boolean shareWallet(Boolean isShared){
        this.getPersonalWallet().setShared(isShared);
        return isShared;
    }
}
