package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.wallet.Wallet;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int price;

    @OneToOne
    private Wallet wallet;
}
