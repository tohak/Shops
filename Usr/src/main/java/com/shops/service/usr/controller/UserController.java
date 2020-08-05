package com.shops.service.usr.controller;

import com.shops.service.usr.domain.modal.CreateUserModal;
import com.shops.service.usr.dto.UserEntityDto;
import com.shops.service.usr.service.UserService;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "This is UserMicroservice";
    }

    @PostMapping("save")
    public String saveUser(@Valid @RequestBody CreateUserModal createUserModal) {
        UserEntityDto save = userService.saveUserEntity(createUserModal);
        return "Save user - " + save.getName() + " ID : " + save.toString();
    }


}
