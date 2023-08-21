package com.NoviBackend.WalletWatch.wallet;

import com.NoviBackend.WalletWatch.wallet.dto.WalletDto;
import com.NoviBackend.WalletWatch.wallet.mapper.WalletMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletService(WalletRepository walletRepository,
                         WalletMapper walletMapper){
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    public List<WalletDto> getAllPublicWallets() {
        List<Wallet> walletList = walletRepository.findWalletBySharedIsTrue();
        if(walletList.isEmpty())
            return null;


        return walletMapper.convertWalletToDtoList(walletList);
    }

    public Wallet findPublicById(int id) {
        Optional<Wallet> wallet = walletRepository.findWalletBySharedIsTrueAndId(id);
        return wallet.orElse(null);
    }
}
