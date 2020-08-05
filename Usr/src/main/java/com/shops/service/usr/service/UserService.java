package com.shops.service.usr.service;

import com.shops.service.usr.config.Inscription;
import com.shops.service.usr.domain.UserEntity;
import com.shops.service.usr.domain.modal.CreateUserModal;
import com.shops.service.usr.dto.UserEntityDto;
import com.shops.service.usr.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService extends AbstractService<UserEntity, UserRepository> {


    private final ModelMapper modelMapper;
    private final Inscription inscription;

    public UserService(ModelMapper modelMapper, Inscription inscription) {
        this.modelMapper = modelMapper;
        this.inscription = inscription;
    }

    public UserEntityDto saveUserEntity(CreateUserModal createUserModal) {
        UserEntity map = modelMapper.map(createUserModal, UserEntity.class);
        map.setEncryptPassword(inscription.getPasswordEncoder().encode(createUserModal.getPassword()));
        log.info("save user "+createUserModal.toString());
        return modelMapper.map(save(map), UserEntityDto.class);
    }

    public Optional<UserEntity> getUserEntity(Long id) {
        return findById(id);
    }


}
