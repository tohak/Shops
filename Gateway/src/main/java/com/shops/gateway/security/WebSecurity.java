package com.shops.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment;

    @Autowired
    public WebSecurity(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        //Вот это вообще прикольная тема, она позволяет как раз таки настроить те запросы в которых токен авторизация не нужен

        http.authorizeRequests()
                .antMatchers(environment.getProperty("api.h2console.url.path")).permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty("api.registration.url.path")).permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty("api.login.url.path")).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager(), environment));


        /*Как я понял, то это свойство делает так, чтобы для каждой формы клиент должен был бы пройти авторизацию
        В итоге это позволяет не кешировать в формах авторизацию.
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
