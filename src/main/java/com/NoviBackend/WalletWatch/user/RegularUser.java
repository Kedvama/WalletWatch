package com.NoviBackend.WalletWatch.user;

import jakarta.persistence.Entity;

@Entity
public class RegularUser extends AbstractUsers{

    public RegularUser() {
    }

    public RegularUser(String username, String firstName, String surname, String emailAddress) {
        super(username, firstName, surname, emailAddress);
    }

    public String becomeProfessionalUsers(){
        return null;
    }
}
