package com.NoviBackend.WalletWatch.user.regular;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UniqueAlreadyExistsException;
import com.NoviBackend.WalletWatch.request.RequestPromote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class RegularUserController {

    private final RegularUserService regularUserService;

    public RegularUserController(RegularUserService regularUserService){
        this.regularUserService = regularUserService;
    }

    @GetMapping("/users")
    public List<RegularUser> getAllUsers(){
        return regularUserService.findAllRegularUsers();
    }

    @GetMapping("/user")
    public String welcomeUser(){
        return "Welcome!";
    }

    @GetMapping("/user/{id}")
    public RegularUser goToPersonalPage(@PathVariable Long id){
        RegularUser user = regularUserService.findById(id);
        if(user == null)
            throw new EntityNotFoundException("User with id: " + id + ", not found.");

        return user;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody RegularUser user) {
        Long userId = regularUserService.createUser(user);

        if(userId == -1) {
            throw new UniqueAlreadyExistsException("Username :" + user.getUsername() + ", already exists");
        }else if (userId == -2) {
            throw new UniqueAlreadyExistsException("Email address:" + user.getEmailAddress() + ", already in use");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/user/{id}")
                .buildAndExpand(userId).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/user/{id}/promote")
    public ResponseEntity<Object> promoteToProfessional(@PathVariable Long id,
                                                        @RequestBody RequestPromote requestPromote){

        // check if user is professional already
        Long profId = regularUserService.promoteUser(requestPromote, id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/prof/{id}")
                .buildAndExpand(profId).toUri();

        return ResponseEntity.created(location).build();
    }
}

/*
TODO

promoteToProffesional:
- userId inside the userService.promoteUser() will be the id of the logged-in user
- cant go to this page if you are an prof already

goToPersonalPage:
- should only go to personal wallet and account
 */