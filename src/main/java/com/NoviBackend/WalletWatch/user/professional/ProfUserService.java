package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.user.dto.RegularUserDto;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUserRepository;
import com.NoviBackend.WalletWatch.user.regular.RegularUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfUserService {
    private final ProfUserRepository profUserRepository;
    private final UserMapper userMapper;
    private final RegularUserRepository regularUserRepository;

    public ProfUserService(ProfUserRepository profUserRepository,
                           UserMapper userMapper,
                           RegularUserRepository regularUserRepository){
        this.profUserRepository = profUserRepository;
        this.userMapper = userMapper;
        this.regularUserRepository = regularUserRepository;
    }

    // find
    public List<ProfessionalUser> findAllProfs() {
        return profUserRepository.findAll();
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

        return regularUser.getId();
    }

    public int existsByUserameAndEmail(RegularUserDto user) {
        if(profUserRepository.existsProfessionalUserByUsername(user.getUsername())){
            return -1;
        } else if (profUserRepository.existsProfessionalUserByEmailAddress(user.getEmailAddress())) {
            return -2;
        }else{
            return 0;
        }
    }
}
