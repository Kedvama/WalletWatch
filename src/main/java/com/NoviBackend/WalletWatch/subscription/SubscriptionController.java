package com.NoviBackend.WalletWatch.subscription;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscriptions")
    public List<Subscription> getAllsharedWallets(){

        return null;
    }

    @GetMapping("/subscription")
    public List<Subscription> getSubscribedTo(Authentication auth){
        List<Subscription> subs = subscriptionService.getSubscriptions(auth.getName());

        return subs;
    }
}
