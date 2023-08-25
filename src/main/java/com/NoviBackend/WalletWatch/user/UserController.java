package com.NoviBackend.WalletWatch.user;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UniqueAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody RegularUser user) {
        Long userId = userService.createUser(user);

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

    @GetMapping("/users")
    public List<RegularUser> getAllUsers(){
        return userService.getAllUsers();
    }


    @GetMapping("/user/{id}")
    public RegularUser getPublicWalletById(@PathVariable Long id){
        RegularUser user = userService.findPublicById(id);
        if(user == null)
            throw new EntityNotFoundException("User with id: " + id + ", not found.");

        return user;
    }
}
