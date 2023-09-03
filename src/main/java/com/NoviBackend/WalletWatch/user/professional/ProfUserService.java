package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.security.AuthenticationService;
import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfUserService {
    private final ProfUserRepository profUserRepository;
    private final UserMapper userMapper;
    private final RegularUserRepository regularUserRepository;
    private final AuthenticationService authenticationService;

    public ProfUserService(ProfUserRepository profUserRepository,
                           UserMapper userMapper,
                           RegularUserRepository regularUserRepository,
                           AuthenticationService authenticationService){
        this.profUserRepository = profUserRepository;
        this.userMapper = userMapper;
        this.regularUserRepository = regularUserRepository;
        this.authenticationService = authenticationService;
    }

    // find
    public List<ProfessionalUsersDto> findAllProfsDto() {
        List<ProfessionalUser>  listProfessionals = profUserRepository.findAll();

        // map users to Dto's
        if(listProfessionals == null){
            return null;
        }

        return userMapper.convertListProfToListProfDto(listProfessionals);
    }

    public ProfessionalUser findProfById(Long id) {
        Optional<ProfessionalUser> prof = profUserRepository.findById(id);
        return prof.orElse(null);
    }

    // create
    public long createProfessionalUser(RegularUser regularUser, RequestPromote request){
        // created
        ProfessionalUser professionalUser = userMapper.convertRegularUserToProfessional(regularUser);

        // set extra attributes
        professionalUser.setCompany(request.getCompany());
        professionalUser.setShortIntroduction(request.getIntroduction());

        // save into the profUser database
        profUserRepository.save(professionalUser);

        // change authority to prof
        authenticationService.changeRole(professionalUser.getUsername(), "ROLE_PROF", "ROLE_USER");

        return professionalUser.getId();
    }

    // delete
    public void deleteProfessionalUser(ProfessionalUser professionalUser){
        profUserRepository.delete(professionalUser);
    }

    // methods
    public Long demoteProfToRegularUser(Long profId){
        // get professionalUser
        ProfessionalUser prof =  findProfById(profId);

        //convert prof to regularUser
        RegularUser regularUser = userMapper.convertProfessionalToRegularUser(prof);

        // delete prof
        deleteProfessionalUser(prof);

        // save regularUser
        regularUserRepository.save(regularUser);

        // change authority
        authenticationService.changeRole(regularUser.getUsername(), "ROLE_USER", "ROLE_PROF");

        return regularUser.getId();
    }

    public int existsByUserameAndEmail(RegularUserCreationDto user) {
        if(profUserRepository.existsProfessionalUserByUsername(user.getUsername())){
            return -1;
        } else if (profUserRepository.existsProfessionalUserByEmailAddress(user.getEmailAddress())) {
            return -2;
        }else{
            return 0;
        }
    }

    public ProfessionalUser findByUsername(String username) {
        Optional<ProfessionalUser> user = profUserRepository.findProfessionalUserByUsername(username);

        if(user.isEmpty()){
            return null;
        }

        return user.get();
    }
}
