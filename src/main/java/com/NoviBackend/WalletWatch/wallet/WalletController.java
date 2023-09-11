package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.request.RequestShareWallet;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.RegularPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService){
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<Object> shareUnshareWalletAsProf(@RequestBody RequestShareWallet shareWallet,
                                                           Authentication auth){
        boolean shared = walletService.shareOrUnshareProfWallet(auth.getName(), auth.getAuthorities(), shareWallet);
        return ResponseEntity.ok().body(shared);
    }

    @GetMapping("/wallets")
    public List<WalletDto> getAllWallets(){
        List<WalletDto> walletList = walletService.getAllWallets();

        if(walletList == null)
            throw new EntityNotFoundException("No wallets found.");

        return walletList;
    }

    @GetMapping("/wallets/{id}")
    public Wallet getPublicWalletById(@PathVariable Long id){
        Wallet wallet = walletService.findWalletById(id);
        if(wallet == null)
            throw new EntityNotFoundException("Shared wallet id: " + id + ", not found.");

        return wallet;
    }

    @GetMapping("/user/wallet")
    public RegularPersonalWalletDto getPersonWalletRegular(Authentication auth){
        RegularPersonalWalletDto walletDto = walletService.getRegularPersonalWalletDto(auth.getName());

        if(walletDto == null){
            throw new EntityNotFoundException("No wallet found for user: " + auth.getName());
        }

        return walletDto;
    }

    @GetMapping("/prof/wallet")
    public ProfPersonalWalletDto getPersonalWalletProf(Authentication auth){
        ProfPersonalWalletDto walletDto = walletService.getProfPersonalWalletDto(auth.getName());

        if(walletDto == null){
            throw new EntityNotFoundException("No wallet found for user: " + auth.getName());
        }

        return walletDto;
    }
}


