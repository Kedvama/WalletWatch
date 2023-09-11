package com.NoviBackend.WalletWatch.user.regular;

import com.NoviBackend.WalletWatch.user.AbstractUsers;
import jakarta.persistence.Entity;

@Entity
public class RegularUser extends AbstractUsers {

    public RegularUser() {
        super();
    }

    public RegularUser(String username, String firstName, String surname, String emailAddress) {
        super(username, firstName, surname, emailAddress);
    }

}
