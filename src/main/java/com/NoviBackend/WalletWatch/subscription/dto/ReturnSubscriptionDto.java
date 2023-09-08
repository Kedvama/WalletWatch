package com.NoviBackend.WalletWatch.subscription.dto;

import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;

public class ReturnSubscriptionDto  {
    private ProfessionalUsersDto prof;
    private ProfPersonalWalletDto wallet;

    public ReturnSubscriptionDto(ProfessionalUsersDto prof, ProfPersonalWalletDto walletDto) {
        this.prof = prof;
        this.wallet = walletDto;
    }

    public ReturnSubscriptionDto() {
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
