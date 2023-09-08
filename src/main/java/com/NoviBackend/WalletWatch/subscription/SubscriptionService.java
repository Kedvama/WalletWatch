package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UnableToSubscribeException;
import com.NoviBackend.WalletWatch.request.RequestSubscribe;
import com.NoviBackend.WalletWatch.subscription.dto.ReturnSubscriptionDto;
import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.mapper.WalletMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final RegularUserRepository regularUserRepository;
    private final RegularUserService regularUserService;
    private final ProfUserService profUserService;
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               RegularUserService regularUserService,
                               ProfUserService profUserService,
                               RegularUserRepository regularUserRepository,
                               UserMapper userMapper,
                               WalletMapper walletMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.regularUserService = regularUserService;
        this.profUserService = profUserService;
        this.regularUserRepository = regularUserRepository;
        this.userMapper = userMapper;
        this.walletMapper = walletMapper;
    }

    public List<ReturnSubscriptionDto> getSubscriptions(String name) {
        RegularUser user = regularUserService.findByUsername(name);
        List<Subscription> subscriptions = user.getSubscriptions();

        if(subscriptions == null){
            return null;
        }

        List<ReturnSubscriptionDto> listRetSub = new ArrayList<>();

        for(Subscription sub: subscriptions){
            ProfessionalUsersDto profDto = userMapper.convertProfToProfDto(sub.getProfessionalUser());
            ProfPersonalWalletDto profWalletDto = walletMapper
                    .convertWalletToProfWalletDto(sub
                    .getProfessionalUser()
                    .getPersonalWallet());

            listRetSub.add(
                    new ReturnSubscriptionDto(
                            profDto,
                            profWalletDto));
        }

        return  listRetSub;
    }

    public Long subscribeToProf(RequestSubscribe subscribeRequest, String username) {
        // find prof to subscribe to.
        ProfessionalUser profToSubscribeTo = profUserService.findProfByUsername(
                subscribeRequest.getSubscribeToUsername());

        if(profToSubscribeTo == null){
            throw new EntityNotFoundException("No professional with username: "
                    + subscribeRequest.getSubscribeToUsername()
                    + ", found");
        }
        // see if wallet is shared.
        if(!profToSubscribeTo.getPersonalWallet().getShared()){
            throw new UnableToSubscribeException("Unable to subscribe to username: "
                    + subscribeRequest.getSubscribeToUsername());
        }

        // find user
        RegularUser regularUser = regularUserService.findByUsername(username);

        // create subscription
        Subscription subscription = createSubscription(profToSubscribeTo);

        // add to subscriptions
        regularUser.addSubscriptions(subscription);
        regularUserRepository.save(regularUser);
        // return profId
        return subscription.getId();
    }


    public Subscription createSubscription(ProfessionalUser profToSubscribeTo){
        Subscription subscription = new Subscription(profToSubscribeTo);

        subscriptionRepository.save(subscription);

        return subscription;
    }

}
