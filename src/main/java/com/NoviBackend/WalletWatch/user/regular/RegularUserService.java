package com.NoviBackend.WalletWatch.user.regular;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.security.AuthenticationService;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import com.NoviBackend.WalletWatch.user.dto.RegularUserDto;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.professional.ProfUserRepository;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.wallet.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegularUserService {
    private final RegularUserRepository regularUserRepository;
    private final WalletService walletService;
    private final ProfUserService profUserService;
    private final UserMapper userMapper;
    private final AuthenticationService authService;


    public RegularUserService(RegularUserRepository regularUserRepository,
                              WalletService walletService,
                              ProfUserService profUserService,
                              UserMapper userMapper,
                              AuthenticationService authService){
        this.regularUserRepository = regularUserRepository;
        this.walletService = walletService;
        this.profUserService = profUserService;
        this.userMapper = userMapper;
        this.authService = authService;
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

    public long usernameEmailCheck(RegularUserCreationDto user) {

        if(regularUserRepository.existsRegularUserByUsername(user.getUsername())){
            return -1;
        } else if (regularUserRepository.existsRegularUserByEmailAddress(user.getEmailAddress())) {
            return -2;
        }

        // check if username or email in ProfessionalUser
        return profUserService.existsByUserameAndEmail(user);
    }

    public long createUser(RegularUserCreationDto userDto){
        Long id = usernameEmailCheck(userDto);

        if(id != 0)
            return id;

        // map to user if ok
        RegularUser regularUser = userMapper.convertRegularUserCreationDtoToRegularUser(userDto);

        // set wallet
        regularUser.setPersonalWallet(walletService.createWallet());

        // save to regular user
        regularUserRepository.save(regularUser);

        // save to auth and security user
        authService.saveRegularUser(userDto);

        return regularUser.getId();
    }

    public Long promoteUser(RequestPromote request, String username) {
        RegularUser regularUser = findByUsername(username);

        if(regularUser == null){
            return null;
        }

        Long profId = profUserService.createProfessionalUser(regularUser, request);
        removeRegularUser(regularUser);

        return profId;
    }

    public RegularUser findByUsername(String username) {
        Optional<RegularUser> user = regularUserRepository.findRegularUserByUsername(username);

        if(user.isEmpty()){
            return null;
        }

        return user.get();
    }

    public RegularUserDto getRegularUserDto(String username) {
        RegularUser user = findByUsername(username);

        if(user == null){
            return null;
        }

        // map user to RegularUserDto
        RegularUserDto userDto = userMapper.convertRegularUserToRegularUserDto(user);

        return userDto;
    }
}


