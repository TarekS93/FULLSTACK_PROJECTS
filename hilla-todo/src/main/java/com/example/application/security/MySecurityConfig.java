package com.example.application.security;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@KeycloakConfiguration
public class MySecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth)
                        throws Exception {
                KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
                keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
                auth.authenticationProvider(keycloakAuthenticationProvider);
        }

        @Bean
        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
                return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        }

        @Override
        protected void configure(HttpSecurity http)
                        throws Exception {
                http.httpBasic().disable();
                http.formLogin().disable();
                // disable spring security csrf as Vaadin already provides this
                // also possible to disable this in Vaadin and leave this enabled
                http.csrf().disable();
                http.headers().frameOptions().disable();
                http
                                .authorizeRequests()
                                .antMatchers("/vaadinServlet/UIDL/**").permitAll()
                                .antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll()
                                .anyRequest().authenticated();
                http
                                .logout()
                                .addLogoutHandler(keycloakLogoutHandler())
                                .logoutUrl("/sso/logout").permitAll()
                                .logoutSuccessUrl("/");
                http
                                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                                .addFilterBefore(keycloakAuthenticationProcessingFilter(),
                                                BasicAuthenticationFilter.class);
                http
                                .exceptionHandling()
                                .authenticationEntryPoint(authenticationEntryPoint());
                http
                                .sessionManagement()
                                .sessionAuthenticationStrategy(sessionAuthenticationStrategy());
        }

        @Bean
        public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
                return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
        }

}
