package com.NoviBackend.WalletWatch.user.mapper;

import com.NoviBackend.WalletWatch.user.dto.PersonalProfessionalUserDto;
import com.NoviBackend.WalletWatch.user.dto.ProfessionalUsersDto;
import com.NoviBackend.WalletWatch.user.dto.RegularUserCreationDto;
import com.NoviBackend.WalletWatch.user.dto.RegularUserDto;
import com.NoviBackend.WalletWatch.user.professional.ProfessionalUser;
import com.NoviBackend.WalletWatch.user.regular.RegularUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public ProfessionalUser convertRegularUserToProfessional(RegularUser regularUser){
        return modelMapper.map(regularUser, ProfessionalUser.class);
    }

    public RegularUser convertProfessionalToRegularUser(ProfessionalUser professionalUser){
        return modelMapper.map(professionalUser, RegularUser.class);
    }

    public RegularUser convertRegularUserCreationDtoToRegularUser(RegularUserCreationDto regularUserDto){
        return modelMapper.map(regularUserDto, RegularUser.class);
    }

    public RegularUserDto convertRegularUserToRegularUserDto(RegularUser regularUser){
        return modelMapper.map(regularUser, RegularUserDto.class);
    }

    public ProfessionalUsersDto convertProfToProfDto(ProfessionalUser prof){
        return modelMapper.map(prof, ProfessionalUsersDto.class);
    }

    public List<ProfessionalUsersDto> convertListProfToListProfDto(List<ProfessionalUser> listProfessionals) {
        List<ProfessionalUsersDto> listProfDto = new ArrayList<>();

        for(ProfessionalUser prof: listProfessionals){
            listProfDto.add(modelMapper.map(prof, ProfessionalUsersDto.class));
        }

        return listProfDto;
    }

    public PersonalProfessionalUserDto convertProfToPersonalProfDto(ProfessionalUser prof){
        return modelMapper.map(prof, PersonalProfessionalUserDto.class);
    }
}
