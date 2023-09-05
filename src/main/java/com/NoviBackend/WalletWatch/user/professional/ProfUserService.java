package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.security.AuthenticationService;
import com.NoviBackend.WalletWatch.subscription.Subscription;
import com.NoviBackend.WalletWatch.subscription.SubscriptionRepository;
import com.NoviBackend.WalletWatch.user.dto.PersonalProfessionalUserDto;
import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import com.NoviBackend.WalletWatch.wallet.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProfUserService {
    private final ProfUserRepository profUserRepository;
    private final UserMapper userMapper;
    private final RegularUserRepository regularUserRepository;
    private final AuthenticationService authenticationService;
    private final SubscriptionRepository subscriptionRepository;
    private final WalletRepository walletRepository;

    public ProfUserService(ProfUserRepository profUserRepository,
                           UserMapper userMapper,
                           RegularUserRepository regularUserRepository,
                           AuthenticationService authenticationService,
                           SubscriptionRepository subscriptionRepository,
                           WalletRepository walletRepository){
        this.profUserRepository = profUserRepository;
        this.regularUserRepository = regularUserRepository;
        this.authenticationService = authenticationService;
        this.subscriptionRepository = subscriptionRepository;
        this.userMapper = userMapper;
        this.walletRepository = walletRepository;
    }

    // find
    public List<ProfessionalUsersDto> findAllProfsDto(Collection<? extends GrantedAuthority> authorities) {

        List<ProfessionalUser> listProfessionals = new ArrayList<>();

        // if admin return all, else return with shared wallet
        if(authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))){
            listProfessionals = profUserRepository.findAll();
        }else{
            List<ProfessionalUser> allProfessionalUsers = profUserRepository.findAll();
            for(ProfessionalUser prof: allProfessionalUsers){
                if(prof.getPersonalWallet().getShared()){
                    listProfessionals.add(prof);
                }
            }
        }
        if(listProfessionals == null){
            return null;
        }

        return userMapper.convertListProfToListProfDto(listProfessionals);
    }

    public ProfessionalUser findProfById(Long id) {
        Optional<ProfessionalUser> prof = profUserRepository.findById(id);
        return prof.orElse(null);
    }

    public ProfessionalUser findProfByUsername(String username){
        Optional<ProfessionalUser> prof = profUserRepository.findProfessionalUserByUsername(username);

        return prof.orElse(null);

    }

    // create
    public long createProfessionalUser(RegularUser regularUser, RequestPromote request){
        // created
        ProfessionalUser professionalUser = userMapper.convertRegularUserToProfessional(regularUser);

        // set extra attributes
        professionalUser.deleteSubscriptions();
        professionalUser.setCompany(request.getCompany());
        professionalUser.setShortIntroduction(request.getIntroduction());

        List<Subscription> subscriptions = regularUser.getSubscriptions();
        subscriptionRepository.deleteAll(subscriptions);
        regularUserRepository.delete(regularUser);
        profUserRepository.save(professionalUser);

        // change authority to prof
        authenticationService.changeRole(professionalUser.getUsername(), "ROLE_PROF", "ROLE_USER");

        return professionalUser.getId();
    }

    // delete
    public void deleteProfessionalUser(ProfessionalUser professionalUser){
        profUserRepository.delete(professionalUser);
    }

    // methods
    @Transactional
    public Long demoteProfToRegularUser(String username){
        // get professionalUser
        ProfessionalUser prof =  findProfByUsername(username);

        // unshare wallet
        prof.shareWallet(false);
        walletRepository.save(prof.getPersonalWallet());

        // delete prof from subscription
        int subs = subscriptionRepository.countByProfessionalUser(prof);
        if(subs != 0){
            subscriptionRepository.deleteAllByProfessionalUser(prof);
        }

        // delete prof
        deleteProfessionalUser(prof);

        //convert prof to regularUser
        RegularUser regularUser = userMapper.convertProfessionalToRegularUser(prof);

        // save regularUser
        regularUserRepository.save(regularUser);

        // change authority
        authenticationService.changeRole(regularUser.getUsername(), "ROLE_USER", "ROLE_PROF");

        return regularUser.getId();
    }

    public int existsByUsernameAndEmail(RegularUserCreationDto user) {
        if(profUserRepository.existsProfessionalUserByUsername(user.getUsername())){
            return -1;
        } else if (profUserRepository.existsProfessionalUserByEmailAddress(user.getEmailAddress())) {
            return -2;
        }else{
            return 0;
        }
    }

    public ProfessionalUser findByUsername(String username) {
        Optional<ProfessionalUser> user = profUserRepository.findProfessionalUserByUsername(username);

        return user.orElse(null);
    }

    public PersonalProfessionalUserDto getProfProfile(String username) {
        ProfessionalUser prof = findProfByUsername(username);

        if(prof == null){
            return null;
        }

        PersonalProfessionalUserDto profDto = userMapper.convertProfToPersonalProfDto(prof);
        profDto.setSubscriptionsQuantity(
                subscriptionRepository.countByProfessionalUser(prof));

        return profDto;
    }
}
