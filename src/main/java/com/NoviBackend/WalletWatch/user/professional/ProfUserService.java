package com.NoviBackend.WalletWatch.user.professional;

import com.NoviBackend.WalletWatch.request.RequestPromote;
import com.NoviBackend.WalletWatch.user.mapper.UserMapper;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfUserService {
    private final ProfUserRepository profUserRepository;
    private final UserMapper userMapper;

    public ProfUserService(ProfUserRepository profUserRepository, UserMapper userMapper){
        this.profUserRepository = profUserRepository;
        this.userMapper = userMapper;
    }

    public List<ProfessionalUser> findAllProfs() {
        return profUserRepository.findAll();
    }

    public ProfessionalUser findProfById(Long id) {
        Optional<ProfessionalUser> prof = profUserRepository.findById(id);
        return prof.orElse(null);
    }

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
}
