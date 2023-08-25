package com.NoviBackend.WalletWatch.user.regular;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.wallet.WalletService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegularUserService {
    private final RegularUserRepository regularUserRepository;
    private final WalletService walletService;
    private final UserMapper userMapper;
    private final ProfUserService profUserService;


    public RegularUserService(RegularUserRepository regularUserRepository, WalletService walletService,
                              UserMapper userMapper, ProfUserService profUserService){
        this.regularUserRepository = regularUserRepository;
        this.walletService = walletService;
        this.userMapper = userMapper;
        this.profUserService = profUserService;
    }

    public RegularUser findById(Long id) {
        Optional<RegularUser> user = regularUserRepository.findById(id);
        return user.orElse(null);
    }

    public List<RegularUser> findAllRegularUsers() {
        return regularUserRepository.findAll();
    }

    public long createUser(RegularUser user) {
        // create wallet and set it for user.
        try{
            user.setPersonalWallet(walletService.createWallet());
            regularUserRepository.save(user);
        }catch (DataIntegrityViolationException ex){
            // remove the created wallet

            // checks which column causes the DataIntegrityViolationException
            if(regularUserRepository.existsRegularUserByUsername(user.getUsername())){
                return -1;
            } else if (regularUserRepository.existsRegularUserByEmailAddress(user.getEmailAddress())) {
                return -2;
            }
        }
        return user.getId();
    }

    public Long promoteUser(RequestPromote request, long userId) {
        RegularUser reUser = this.findById(userId);
        Long profId = profUserService.createProfessionalUser(reUser, request);
        return profId;
    }
}

/*
TODO

createUser:
- remove the created wallet when an error is thrown
 */