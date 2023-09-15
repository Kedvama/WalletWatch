package com.NoviBackend.WalletWatch.user.regular;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.exception.UniqueAlreadyExistsException;
import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import com.NoviBackend.WalletWatch.user.dto.RegularUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/user")
    public RegularUserDto goToPersonalPage(Authentication auth){
        RegularUserDto regularUserDto = regularUserService.getRegularUserDto(auth.getName());

        if(regularUserDto == null)
            throw new EntityNotFoundException("username: " + auth.getName() + ", not found");

        return regularUserDto;
    }

    @GetMapping("/users")
    public List<RegularUser> getAllUsers(){
        return regularUserService.findAllRegularUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody @Validated RegularUserCreationDto userCreationDto) {
        // check username and password
        regularUserService.usernameEmailCheck(userCreationDto);

        Long userId = regularUserService.createUser(userCreationDto);

        if(userId == -1) {
            throw new UniqueAlreadyExistsException("Username: " + userCreationDto.getUsername() + ", already exists");
        }else if (userId == -2) {
            throw new UniqueAlreadyExistsException("Email address: " + userCreationDto.getEmailAddress() + ", already in use");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/users/{id}")
                .buildAndExpand(userId).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}")
    public RegularUser goToPersonalPage(@PathVariable Long id){
        RegularUser user = regularUserService.findById(id);

        if(user == null)
            throw new EntityNotFoundException("User with id: " + id + ", not found.");

        return user;
    }

    @PostMapping("/user/promote")
    public ResponseEntity<Object> promoteToProfessional(@RequestBody RequestPromote requestPromote,
                                                        Authentication auth){

        // check if user is professional already
        Long profId = regularUserService.promoteUser(requestPromote, auth.getName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/profs/{profId}")
                .buildAndExpand(profId).toUri();

        return ResponseEntity.created(location).build();
    }
}

