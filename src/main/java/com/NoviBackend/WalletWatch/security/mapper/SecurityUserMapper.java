package com.NoviBackend.WalletWatch.security.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserMapper {

    private final ModelMapper modelMapper;

    public SecurityUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
