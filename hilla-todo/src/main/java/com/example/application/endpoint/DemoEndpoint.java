package com.example.application.endpoint;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.example.application.security.RequiresAdminRole;

import dev.hilla.Endpoint;

@Endpoint
@RolesAllowed("ROLE_user")
public class DemoEndpoint {

    @PermitAll // Spring Security's definition of permitAll: allow annoymous
    public String methodOne(String name) {
        return "Hello " + name + "!";
    }

    // rule inherited from class
    public String methodTwo(String name) {
        return "Greetings, " + name + "!";
    }

    /* NOT WORKING */
    @RequiresAdminRole // a custom annotation
    public String methodThree(String name) {
        return "Good bye, " + name + "!";
    }

}
