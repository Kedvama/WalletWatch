package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
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
    private final ProfUserService profUserService;

    public WalletService(WalletRepository walletRepository,
                         WalletMapper walletMapper,
                         @Lazy RegularUserService regularUserService,
                         ProfUserService profUserService){
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.regularUserService = regularUserService;
        this.profUserService = profUserService;
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

    public ProfPersonalWalletDto getProfPersonalWalletDto(String username) {
        ProfessionalUser profUser = profUserService.findByUsername(username);

        if(profUser == null){
            return null;
        }

        ProfPersonalWalletDto walletDto = walletMapper.convertWalletToProfWalletDto(
                profUser.getPersonalWallet()
        );

        return walletDto;
    }
}
