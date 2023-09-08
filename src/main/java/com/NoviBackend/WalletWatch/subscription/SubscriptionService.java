package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UnableToSubscribeException;
import com.NoviBackend.WalletWatch.request.RequestSubscribe;
import com.NoviBackend.WalletWatch.request.RequestUnSubscribe;
import com.NoviBackend.WalletWatch.subscription.dto.SubscribedProfessionalDto;
import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import com.NoviBackend.WalletWatch.wallet.dto.ProfPersonalWalletDto;
import com.NoviBackend.WalletWatch.wallet.mapper.WalletMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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

    public List<SubscribedProfessionalDto> getSubscriptions(String name) {
        RegularUser user = regularUserService.findByUsername(name);
        List<Subscription> subscriptions = user.getSubscriptions();

        if(subscriptions == null){
            return null;
        }

        return  mapSubscriptionToDto(subscriptions);
    }

    public List<SubscribedProfessionalDto> getAllSubscriptions() {
        List<Subscription> subscription = subscriptionRepository.findAll();
        return mapSubscriptionToDto(subscription);
    }

    public Long subscribeToProf(RequestSubscribe subscribeRequest, String username) {
        // find and check prof to subscribe to.
        ProfessionalUser profToSubscribeTo = checkProf(subscribeRequest);

        // find user
        RegularUser regularUser = regularUserService.findByUsername(username);

        // create subscription
        Subscription subscription = createSubscription(profToSubscribeTo, regularUser);

        // return profId
        return subscription.getId();
    }

    public String unSubscribe(RequestUnSubscribe unSubscribe, Authentication auth) {
        RegularUser user = regularUserService.findByUsername(auth.getName());

        // check if user is subscribed to prof
        Subscription subscription = profInSubscriptionsCheck(user, unSubscribe.getUsernameProf());

        if(subscription == null){
            return null;
        }

        deleteSubscription(user, subscription);

        // return profUsername
        return unSubscribe.getUsernameProf();
    }

    public Subscription profInSubscriptionsCheck(RegularUser user, String unsubscribeUsername){

        // see if prof inside users subscriptions
        Predicate<? super Subscription> predicate =
                subscription -> subscription.getProfessionalUser()
                        .getUsername()
                        .equals(unsubscribeUsername);

        Optional<Subscription> optionalSubscription = user.getSubscriptions()
                .stream()
                .filter(predicate)
                .findFirst();

        return optionalSubscription.orElse(null);
    }

    public Subscription createSubscription(ProfessionalUser profToSubscribeTo, RegularUser regularUser){
        Subscription subscription = new Subscription(profToSubscribeTo);
        subscriptionRepository.save(subscription);

        regularUser.addSubscriptions(subscription);
        regularUserRepository.save(regularUser);

        return subscription;
    }

    public void deleteSubscription(RegularUser user, Subscription subscription){
        // delete subscription from user
        user.removeSubscription(subscription);
        regularUserRepository.save(user);

        // delete subscription
        subscriptionRepository.delete(subscription);
    }

    public ProfessionalUser checkProf(RequestSubscribe subscribeRequest){
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

        return profToSubscribeTo;
    }

    public List<SubscribedProfessionalDto> mapSubscriptionToDto(List<Subscription> subscriptions){
        List<SubscribedProfessionalDto> listRetSub = new ArrayList<>();

        for(Subscription sub: subscriptions){
            ProfessionalUsersDto profDto = userMapper.convertProfToProfDto(sub.getProfessionalUser());
            ProfPersonalWalletDto profWalletDto = walletMapper
                    .convertWalletToProfWalletDto(sub
                            .getProfessionalUser()
                            .getPersonalWallet());

            listRetSub.add(
                    new SubscribedProfessionalDto(
                            profDto,
                            profWalletDto));
        }
        return listRetSub;
    }
}
