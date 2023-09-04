package com.NoviBackend.WalletWatch.subscription;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscription")
    public List<Subscription> getAllSharedWallets(Authentication auth){
        List<Subscription> subs = subscriptionService.getSubscriptions(auth.getName());

        return subs;
    }
}
