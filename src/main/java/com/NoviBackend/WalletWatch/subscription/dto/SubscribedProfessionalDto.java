package com.NoviBackend.WalletWatch.subscription.dto;

import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;

public class SubscribedProfessionalDto {
    private Long id;
    private ProfessionalUsersDto prof;
    private ProfPersonalWalletDto wallet;

    public SubscribedProfessionalDto(Long id, ProfessionalUsersDto prof, ProfPersonalWalletDto walletDto) {
        this.id = id;
        this.prof = prof;
        this.wallet = walletDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubscribedProfessionalDto() {
    }

    public ProfessionalUsersDto getProf() {
        return prof;
    }

    public void setProf(ProfessionalUsersDto prof) {
        this.prof = prof;
    }

    public ProfPersonalWalletDto getWallet() {
        return wallet;
    }

    public void setWallet(ProfPersonalWalletDto wallet) {
        this.wallet = wallet;
    }
}
