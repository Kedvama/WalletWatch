package com.NoviBackend.WalletWatch.wallet.dto;

import jakarta.validation.constraints.NotBlank;

public class WalletDto {

    @NotBlank
    private Long id;

//    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
