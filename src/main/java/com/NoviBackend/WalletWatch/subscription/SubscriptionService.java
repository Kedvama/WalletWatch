package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final RegularUserService regularUserService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               RegularUserService regularUserService) {
        this.subscriptionRepository = subscriptionRepository;
        this.regularUserService = regularUserService;
    }

    public List<Subscription> getSubscriptions(String name) {
        RegularUser user = regularUserService.findByUsername(name);
        return user.getSubscriptions();
    }

    public List<Subscription> getAllSharedProfs() {
        return null;
    }
}
