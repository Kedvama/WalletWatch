package com.NoviBackend.WalletWatch.wallet.mapper;

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

    public WalletDto convertWalletToDto(Wallet wallet){
        return modelMapper.map(wallet, WalletDto.class);
    }

    public List<WalletDto> convertWalletToDtoList(List<Wallet> wallet){
        List<WalletDto> walletDtoList = new ArrayList<>();

        // loop through the list of wallets,
        // map them to a WalletDto and
        // add them to the list
        // return list with dto's
        for(Wallet w: wallet){
            walletDtoList.add(modelMapper.map(w, WalletDto.class));
        }

        return walletDtoList;
    }

    public Wallet convertDtoToWallet(WalletDto walletDto){
        return modelMapper.map(walletDto, Wallet.class);
    }
}
