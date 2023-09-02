package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import com.NoviBackend.WalletWatch.wallet.dto.RegularPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import com.NoviBackend.WalletWatch.wallet.mapper.WalletMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final RegularUserService regularUserService;

    public WalletService(WalletRepository walletRepository,
                         WalletMapper walletMapper,
                         @Lazy RegularUserService regularUserService){
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.regularUserService = regularUserService;
    }

    public Wallet findPublicById(int id) {
        Optional<Wallet> wallet = walletRepository.findWalletBySharedIsTrueAndId(id);
        return wallet.orElse(null);
    }

    public List<WalletDto> getAllPublicWallets() {
        List<Wallet> walletList = walletRepository.findWalletBySharedIsTrue();
        if(walletList.isEmpty())
            return null;


        return walletMapper.convertWalletToDtoList(walletList);
    }

    public Wallet createWallet(){
        Wallet wallet = new Wallet();
        walletRepository.save(wallet);

        return wallet;
    }

    public RegularPersonalWalletDto getRegularPersonalWalletDto(String username) {
        RegularUser regularUser = regularUserService.findByUsername(username);

        if(regularUser == null){
            return null;
        }

        RegularPersonalWalletDto walletDto = walletMapper.convertWalletToRegularWalletDto(
                regularUser.getPersonalWallet()
        );

        return walletDto;
    }
}
