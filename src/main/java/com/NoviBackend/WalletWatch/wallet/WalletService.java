package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.request.RequestShareWallet;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.RegularPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import com.NoviBackend.WalletWatch.wallet.mapper.WalletMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
                         @Lazy ProfUserService profUserService){
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.regularUserService = regularUserService;
        this.profUserService = profUserService;
    }

    public Wallet createWallet(){
        Wallet wallet = new Wallet();
        walletRepository.save(wallet);

        return wallet;
    }

    public Wallet findWalletById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.orElse(null);
    }

    public List<WalletDto> getAllWallets() {
        List<Wallet> walletList = walletRepository.findAll();
        if(walletList.isEmpty())
            return null;

        return walletMapper.convertWalletToDtoList(walletList);
    }

    public RegularPersonalWalletDto getRegularPersonalWalletDto(String username) {
        RegularUser regularUser = regularUserService.findByUsername(username);

        if(regularUser == null){
            return null;
        }

        return walletMapper.convertWalletToRegularWalletDto(
                regularUser.getPersonalWallet());
    }

    public ProfPersonalWalletDto getProfPersonalWalletDto(String username) {
        ProfessionalUser profUser = profUserService.findProfByUsername(username);

        if(profUser == null){
            return null;
        }

        return walletMapper.convertWalletToProfWalletDto(
                profUser.getPersonalWallet()
        );
    }

    public boolean shareOrUnshareProfWallet(String username,
                                            Collection<? extends GrantedAuthority> authorities,
                                            RequestShareWallet shareWallet) {
        boolean shared = false;

        // extra check if user is prof
        if(authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_PROF"))){
            ProfessionalUser prof = profUserService.findProfByUsername(username);
            shared = prof.shareWallet(shareWallet.getShareWallet());
            walletRepository.save(prof.getPersonalWallet());
        }

        return shared;
    }
}
