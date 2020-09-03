package com.shops.service.usr.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shops.service.usr.domain.modal.LoginRequestModal;
import com.shops.service.usr.dto.UserEntityDto;
import com.shops.service.usr.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.apache.catalina.User;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment environment;

    public AuthenticationFilter(UserService userService,
                                Environment environment,
                                AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {

        //Наша модель аутентификации которую получаем из запроса
        LoginRequestModal details = new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequestModal.class);

        //Заполняем аутентификацию данными для дальнейшей передачи
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        details.getEmail(),
                        details.getPassword(),
                        new ArrayList<>()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        //Для начала проверяем то, что пользователь вообще есть
        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserEntityDto userDetailsByEmail = userService.getUserDetailsByEmail(userName);

        //Строим наш токен
        String token = Jwts.builder()
                //Тут как раз закладываем нашего пользователя
                .setSubject(userDetailsByEmail.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        //Заполняем ответ в который уже идёт наш токен
        response.addHeader("token", token);
        response.addHeader("userId", userDetailsByEmail.getUserId());
    }
}
