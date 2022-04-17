package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends VaadinWebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Set default security policy that permits Hilla internal requests and
        // denies all other
        super.configure(http);
        // per autenticazione legacy con form integrata di spring-security
        // http.formLogin().loginPage("/login").permitAll();
        // per autenticazione con vaadin-hilla
        setLoginView(http, "/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configure users and roles in memory
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user")
                .password("$2a$12$U/wUAxjIO7WRZkXtRzumauYc3wfBSK5cMjJ3ACPdzpg1QZ8EqRpdW")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("$2a$12$9Cc.xTaATd8RChEjbOlMjO/3BEVdTxb90Iqvq7s/Y4Kd4Xcd2D9W6")
                .roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/images/**");
    }
}
