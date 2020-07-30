package com.shops.service.usr.service;

import com.shops.service.usr.domain.UserEntity;
import com.shops.service.usr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> getUserEntity(Long id) {
        return userRepository.findById(id);
    }


}
