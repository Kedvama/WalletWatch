package com.NoviBackend.WalletWatch.user;

import com.NoviBackend.WalletWatch.wallet.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private RegularUserRepository regularUserRepository;
    private WalletService walletService;

    public UserService(RegularUserRepository regularUserRepository, WalletService walletService){
        this.regularUserRepository = regularUserRepository;
        this.walletService = walletService;
    }

    public RegularUser findPublicById(Long id) {
        Optional<RegularUser> user = regularUserRepository.findById(id);
        return user.orElse(null);

    }

    public long createUser(RegularUser user) {
        // check if username or email already exist


        // create wallet and set it for user.
        user.setPersonalWallet(walletService.createWallet());
        regularUserRepository.save(user);

        return user.getId();
    }

    public List<RegularUser> getAllUsers() {
        return regularUserRepository.findAll();

    }
}
