package com.NoviBackend.WalletWatch.user.professional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
