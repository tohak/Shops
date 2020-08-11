package com.shops.service.usr.domain.modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestModal {
    private String email;
    private String password;
}
