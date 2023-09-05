package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UnableToSubscribeException;
import com.NoviBackend.WalletWatch.request.RequestSubscribe;
import com.NoviBackend.WalletWatch.user.professional.ProfUserService;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final RegularUserService regularUserService;
    private final ProfUserService profUserService;
    private final RegularUserRepository regularUserRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               RegularUserService regularUserService,
                               ProfUserService profUserService,
                               RegularUserRepository regularUserRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.regularUserService = regularUserService;
        this.profUserService = profUserService;
        this.regularUserRepository = regularUserRepository;
    }

    public List<Subscription> getSubscriptions(String name) {
        RegularUser user = regularUserService.findByUsername(name);
        return user.getSubscriptions();
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
