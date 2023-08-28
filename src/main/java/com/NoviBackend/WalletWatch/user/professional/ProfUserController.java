package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.request.RequestDemote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ProfUserController {
    private final ProfUserService profUserService;

    public ProfUserController(ProfUserService profUserService){
        this.profUserService = profUserService;
    }

    @GetMapping("/profs")
    public List<ProfessionalUser> getAllUsers(){
        return profUserService.findAllProfs();
    }


    @PostMapping("/prof/{profId}/demote")
    public ResponseEntity<Object> demoteProfToRegularUser(@PathVariable Long profId,
                                                          @RequestBody RequestDemote requestDemote){
        Long userId = profUserService.demoteProfToRegularUser(profId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/user/{userId}")
                .buildAndExpand(userId).toUri();

        return ResponseEntity.created(location).build();
    }
}
