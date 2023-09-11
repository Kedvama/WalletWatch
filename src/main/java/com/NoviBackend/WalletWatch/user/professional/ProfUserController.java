package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.exception.EntityNotFoundException;
import com.NoviBackend.WalletWatch.request.RequestDemote;
import com.NoviBackend.WalletWatch.user.dto.PersonalProfessionalUserDto;
import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/prof")
    public PersonalProfessionalUserDto getPersonalProfile(Authentication auth){
        PersonalProfessionalUserDto proDto = profUserService.getProfProfile(auth.getName());

        if(proDto == null){
            throw new EntityNotFoundException("No prof account for: " + auth.getName() + ", found");
        }

        return proDto;
    }

    @PostMapping("/prof/demote")
    public ResponseEntity<Object> demoteProfToRegularUser(@RequestBody RequestDemote requestDemote,
                                                          Authentication auth){
        Long userId = profUserService.demoteProfToRegularUser(auth.getName());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/user/{userId}")
                .buildAndExpand(userId).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/profs")
    public List<ProfessionalUsersDto> getAllProfessionals(Authentication auth){
        List<ProfessionalUsersDto> listProfDto = profUserService.findAllProfsDto(auth.getAuthorities());

        if(listProfDto == null){
            throw new EntityNotFoundException("No professional found");
        }
        return listProfDto;
    }

    @GetMapping("/profs/{id}")
    public ProfessionalUsersDto getAllProfessionals(@PathVariable Long id,
                                                    Authentication auth){
        ProfessionalUsersDto profDto = profUserService.findProfById(id, auth);

        if(profDto == null){
            throw new EntityNotFoundException("No professional with id: " + id + ", found.");
        }
        return profDto;
    }
}
