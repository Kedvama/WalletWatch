package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UnableToSubscribeException;
import com.NoviBackend.WalletWatch.request.RequestSubscribe;
import com.NoviBackend.WalletWatch.subscription.dto.ReturnSubscriptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Object> subscribeToProf(@RequestBody RequestSubscribe subscribeRequest, Authentication auth){
        Long subscriptionId;

        try {
            subscriptionId = subscriptionService.subscribeToProf(subscribeRequest, auth.getName());
        }catch (EntityNotFoundException ex){
            throw ex;
        }catch (UnableToSubscribeException ex){
            throw ex;
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/subscriptions/{id}")
                .buildAndExpand(subscriptionId).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/user/subscriptions")
    public List<ReturnSubscriptionDto> getSubscribedTo(Authentication auth){
        List<ReturnSubscriptionDto> subs = subscriptionService.getSubscriptions(auth.getName());

        if(subs == null){
            throw new EntityNotFoundException("No subscriptions");
        }

        return subs;
    }
}
