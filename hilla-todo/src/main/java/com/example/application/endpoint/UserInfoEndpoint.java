package com.example.application.endpoint;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.security.PermitAll;

import com.example.application.model.UserInfo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import dev.hilla.Endpoint;

/**
 * Provides information about the current user.
 */
// tag::snippet[]
@Endpoint
public class UserInfoEndpoint {

    @PermitAll
    @Nonnull
    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();

        final List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new UserInfo(auth.getName(), authorities);
    }

}
// end::snippet[]