package com.NoviBackend.WalletWatch.subscription;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UnableToSubscribeException;
import com.NoviBackend.WalletWatch.request.RequestSubscribe;
import com.NoviBackend.WalletWatch.request.RequestUnSubscribe;
import com.NoviBackend.WalletWatch.subscription.dto.SubscribedProfessionalDto;
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

    @GetMapping("/subscriptions")
    public List<SubscribedProfessionalDto> getAllSubscriptions(){
        List<SubscribedProfessionalDto> subs = subscriptionService.getAllSubscriptions();

        if(subs.isEmpty()){
            throw new EntityNotFoundException("NO SUBSCRIPTIONS");
        }

        return subs;
    }

    @GetMapping("/subscriptions/{id}")
    public SubscribedProfessionalDto getSubscriptionById(@PathVariable Long id){
        SubscribedProfessionalDto sub = subscriptionService.getSubscriptionById(id);

        if(sub == null){
            throw new EntityNotFoundException("No subscription with id: " + id + ", found.");
        }

        return sub;
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

    @PutMapping("/subscriptions")
    public ResponseEntity<Object> unsubscribeToProf(@RequestBody RequestUnSubscribe unSubscribe, Authentication auth){
        String username  = subscriptionService.unSubscribe(unSubscribe, auth);

        if(username == null)
            throw new EntityNotFoundException("No subscription to professional: " +
                    unSubscribe.getUsernameProf());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/subscriptions")
    public List<SubscribedProfessionalDto> getSubscribedTo(Authentication auth){
        List<SubscribedProfessionalDto> subs = subscriptionService.getSubscriptions(auth.getName());

        if(subs == null){
            throw new EntityNotFoundException("No subscriptions");
        }

        return subs;
    }
}
