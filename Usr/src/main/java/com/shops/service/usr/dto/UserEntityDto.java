package com.shops.service.usr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntityDto {
    private String userId;
    private String name;
    private String email;
    private String password;
}
