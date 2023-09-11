package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import jakarta.persistence.*;


@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProfessionalUser professionalUser;

    public Subscription() {
    }

    public Subscription(ProfessionalUser professionalUser) {
        this.professionalUser = professionalUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfessionalUser getProfessionalUser() {
        return professionalUser;
    }

    public void setProfessionalUser(ProfessionalUser professionalUser) {
        this.professionalUser = professionalUser;
    }
}
