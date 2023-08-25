package com.NoviBackend.WalletWatch.user;

import com.NoviBackend.WalletWatch.subscription.Subscription;
import com.NoviBackend.WalletWatch.wallet.Wallet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.Set;

@MappedSuperclass
public abstract class AbstractUsers {

    // attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String surname;

    @Email
    @Column(nullable = false, unique = true)
    private String emailAddress;

    @OneToOne
    private Wallet personalWallet;

    @OneToMany
    @JoinColumn(name="user_id")
    private Set<Subscription> subscriptions;


    // constructor
    public AbstractUsers(){
    }

    public AbstractUsers(String username, String firstName, String surname, String emailAddress) {
        this.username = username;
        this.firstName = firstName;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.personalWallet = new Wallet();
        // wallet needs to be saved.
    }


    // getters & setters
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

    public Long getId() {
        return id;
    }

    public Wallet getPersonalWallet() {
        return personalWallet;
    }

    public void setPersonalWallet(Wallet wallet) {
        this.personalWallet = wallet;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public String toString() {
        return "AbstractUsers{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", personalWallet=" + personalWallet +
                ", subscriptions=" + subscriptions +
                '}';
    }
}
