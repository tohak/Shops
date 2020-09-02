package com.shops.service.usr.service;

import com.shops.service.usr.config.security.Inscription;
import com.shops.service.usr.domain.UserEntity;
import com.shops.service.usr.domain.modal.CreateUserModal;
import com.shops.service.usr.dto.UserEntityDto;
import com.shops.service.usr.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class UserService extends AbstractService<UserEntity, UserRepository> implements UserDetailsService {


    private final ModelMapper modelMapper;
    private final Inscription inscription;

    public UserService(ModelMapper modelMapper, Inscription inscription) {
        this.modelMapper = modelMapper;
        this.inscription = inscription;
    }

    public UserEntityDto saveUserEntity(CreateUserModal createUserModal) {
        UserEntity map = modelMapper.map(createUserModal, UserEntity.class);
        map.setEncryptPassword(inscription.getPasswordEncoder().encode(createUserModal.getPassword()));
        log.info("save user " + createUserModal.toString());
        return modelMapper.map(save(map), UserEntityDto.class);
    }

    public UserEntityDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = repository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return modelMapper.map(userEntity, UserEntityDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = repository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptPassword(), true, true, true, true, new ArrayList<>());
    }
}
