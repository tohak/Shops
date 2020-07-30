package com.shops.service.usr.controller;

import com.shops.service.usr.domain.UserEntity;
import com.shops.service.usr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "This is UserMicroservice";
    }

    @GetMapping("save")
    public String save() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Pavel");
        UserEntity save = userService.save(userEntity);
        return "Save user - " + save.getName() + " ID : " + save.getUserId();
    }

    @GetMapping("{id}")
    public String getUser(@PathVariable Long id) {
        Optional<UserEntity> userEntity = userService.getUserEntity(id);
        String s = userEntity.isPresent() ? userEntity.get().getName() : "отсутсвует";
        return "User - " + s;
    }

}
