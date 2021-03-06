package com.javaguru.onlineshop.config;

import com.javaguru.onlineshop.user.login.UserLoginService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserLoginService service;

    public WebSecurityConfig(UserLoginService service) {
        this.service = service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/css/*", "/api/v1/registration").permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .formLogin().loginPage("/login").successForwardUrl("/main").permitAll()
            .and()
                .logout().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(service).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
