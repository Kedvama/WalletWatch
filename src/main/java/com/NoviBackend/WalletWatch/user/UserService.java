package com.NoviBackend.WalletWatch.user;

import com.NoviBackend.WalletWatch.wallet.WalletService;
import org.springframework.dao.DataIntegrityViolationException;
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
        // create wallet and set it for user.
        try{
            user.setPersonalWallet(walletService.createWallet());
            regularUserRepository.save(user);
        }catch (DataIntegrityViolationException ex){
            // checks which column causes the DataIntegrityViolationException
            if(regularUserRepository.existsRegularUserByUsername(user.getUsername())){
                return -1;
            } else if (regularUserRepository.existsRegularUserByEmailAddress(user.getEmailAddress())) {
                return -2;
            }
        }
        return user.getId();
    }

    public List<RegularUser> getAllUsers() {
        return regularUserRepository.findAll();

    }
}

