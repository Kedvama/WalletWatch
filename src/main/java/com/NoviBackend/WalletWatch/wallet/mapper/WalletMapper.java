package com.NoviBackend.WalletWatch.wallet.mapper;

import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.RegularPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import com.NoviBackend.WalletWatch.wallet.Wallet;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WalletMapper {
    private final ModelMapper modelMapper;

    public WalletMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public List<WalletDto> convertWalletToDtoList(List<Wallet> wallet){
        List<WalletDto> walletDtoList = new ArrayList<>();

        for(Wallet w: wallet){
            walletDtoList.add(modelMapper.map(w, WalletDto.class));
        }

        return walletDtoList;
    }

    public RegularPersonalWalletDto convertWalletToRegularWalletDto(Wallet wallet){
        return modelMapper.map(wallet, RegularPersonalWalletDto.class);
    }

    public ProfPersonalWalletDto convertWalletToProfWalletDto(Wallet wallet) {
        return modelMapper.map(wallet, ProfPersonalWalletDto.class);
    }
}
