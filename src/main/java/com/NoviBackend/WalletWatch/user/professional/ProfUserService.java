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
import org.springframework.security.core.Authentication;
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

    public long createProfessionalUser(RegularUser regularUser, RequestPromote request){
        ProfessionalUser professionalUser = userMapper.convertRegularUserToProfessional(regularUser);

        professionalUser = setAttributes(professionalUser, request);
        conversionToProf(regularUser, professionalUser);

        // change authority to prof
        authenticationService.changeRole(professionalUser.getUsername(), "ROLE_PROF", "ROLE_USER");

        return professionalUser.getId();
    }

    @Transactional
    public Long demoteProfToRegularUser(String username){
        // get professionalUser
        ProfessionalUser prof =  findProfByUsername(username);

        prof = unshareWallet(prof);
        prof = deleteFromSubscriptions(prof);

        return convertToRegularUser(prof);
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

    public List<ProfessionalUsersDto> findAllProfsDto(Collection<? extends GrantedAuthority> authorities) {
        List<ProfessionalUser> listProfessionals = adminOrNot(profUserRepository.findAll(), authorities);

        if(listProfessionals == null){
            return null;
        }

        return userMapper.convertListProfToListProfDto(listProfessionals);
    }

    public ProfessionalUsersDto findProfById(Long id, Authentication auth) {
        Optional<ProfessionalUser> prof = profUserRepository.findById(id);

        if(prof.isEmpty()){
            return null;
        }

        ProfessionalUser professional = adminOrNot(prof.get(), auth.getAuthorities());

        if(professional == null ) {
            return null;
        }

        return userMapper.convertProfToProfDto(professional);
    }

    public ProfessionalUser findProfByUsername(String username){
        Optional<ProfessionalUser> prof = profUserRepository.findProfessionalUserByUsername(username);

        return prof.orElse(null);
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


    // functions
    private List<ProfessionalUser> adminOrNot(List<ProfessionalUser> listProfessionals,
                                              Collection<? extends GrantedAuthority> authorities) {
        // if admin return all, else return with shared wallet
        if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            return listProfessionals;
        }

        // loop through all the profs and add them when wallet is shared.
        List<ProfessionalUser> sharedProfessionals = new ArrayList<>();

        for (ProfessionalUser prof : listProfessionals) {
            if (prof.getPersonalWallet().getShared()) {
                sharedProfessionals.add(prof);
            }
        }

        return sharedProfessionals;
    }
    private ProfessionalUser adminOrNot(ProfessionalUser prof, Collection<? extends GrantedAuthority> authorities){
        if (authorities.stream()
                .anyMatch(ga -> ga.getAuthority()
                        .equals("ROLE_ADMIN"))
                || prof.getPersonalWallet().getShared()) {
            return prof;
        }

        return null;

    }

    private void conversionToProf(RegularUser regularUser, ProfessionalUser professionalUser){
        List<Subscription> subscriptions = regularUser.getSubscriptions();

        subscriptionRepository.deleteAll(subscriptions);
        regularUserRepository.delete(regularUser);
        profUserRepository.save(professionalUser);
    }

    private Long convertToRegularUser(ProfessionalUser prof){
        //convert prof to regularUser
        RegularUser regularUser = userMapper.convertProfessionalToRegularUser(prof);

        // delete prof
        deleteProfessionalUser(prof);

        // save regularUser
        regularUserRepository.save(regularUser);

        // change authority
        authenticationService.changeRole(regularUser.getUsername(), "ROLE_USER", "ROLE_PROF");

        return regularUser.getId();
    }

    private void deleteProfessionalUser(ProfessionalUser professionalUser){
        profUserRepository.delete(professionalUser);
    }

    private ProfessionalUser deleteFromSubscriptions(ProfessionalUser prof){
        // delete prof from subscription
        int subs = subscriptionRepository.countByProfessionalUser(prof);

        if(subs != 0){
            subscriptionRepository.deleteAllByProfessionalUser(prof);
        }

        return prof;
    }

    private ProfessionalUser setAttributes(ProfessionalUser professionalUser, RequestPromote request){
        professionalUser.deleteSubscriptions();
        professionalUser.setCompany(request.getCompany());
        professionalUser.setShortIntroduction(request.getIntroduction());

        return professionalUser;
    }

    private ProfessionalUser unshareWallet(ProfessionalUser prof){
        // unshare wallet
        prof.shareWallet(false);
        walletRepository.save(prof.getPersonalWallet());

        return prof;
    }
}
