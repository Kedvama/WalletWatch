package com.NoviBackend.WalletWatch.user.regular;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.user.professional.ProfUserRepository;
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
    private final ProfUserService profUserService;


    public RegularUserService(RegularUserRepository regularUserRepository,
                              WalletService walletService,
                              ProfUserService profUserService,
                              ProfUserRepository profUserRepository){
        this.regularUserRepository = regularUserRepository;
        this.walletService = walletService;
        this.profUserService = profUserService;
    }
    public void removeRegularUser(RegularUser user){
        regularUserRepository.delete(user);
    }

    public RegularUser findById(Long id) {
        Optional<RegularUser> user = regularUserRepository.findById(id);
        return user.orElse(null);
    }

    public List<RegularUser> findAllRegularUsers() {
        return regularUserRepository.findAll();
    }

    public long createUser(RegularUser user) {
        // check if username or email in ProfessionalUser
        int available = profUserService.existsByUserameAndEmail(user);

        if(available < 0){
            return available;
        }

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

    public Long promoteUser(RequestPromote request, long userId) {
        RegularUser reUser = findById(userId);
        Long profId = profUserService.createProfessionalUser(reUser, request);
        removeRegularUser(reUser);
        return profId;
    }
}

/*
TODO

createUser:
- remove the created wallet when an error is thrown

 */