package com.shops.gateway.security;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment environment;

    public AuthorizationFilter(AuthenticationManager authManager, Environment environment) {
        super(authManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        //Тут просто идёт проверка, что запрос пришёл с токеном авторизации.
        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null || authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        //Повторно проверяем нашу авторизацию
        String authorizationHeader = req.getHeader(environment.getProperty("authorization.token.header.name"));
        if (authorizationHeader == null) {
            return null;
        }

        //Забираем наш токен
        String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");

        //Json Web Token
        String userId = Jwts.parser()
                //Ключ подписи для рассшифровки
                .setSigningKey(environment.getProperty("token.secret"))
                //Парс токена
                .parseClaimsJws(token)
                //По сути получение достоверного объекта типа Claims
                .getBody()
                .getSubject();
        if (userId == null) {
            return null;
        }
        //В случае успеха возвращаем объект аутентификации для дальнейшей вставки в контекст
        return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
    }
}
