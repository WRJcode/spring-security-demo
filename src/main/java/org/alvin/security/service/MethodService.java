package org.alvin.security.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MethodService {
    @Secured("ROLE_ADMIN")
    public String admin(){
        return "hello admin";
    }

    @PreAuthorize("hasRole('ADMIN') and hasRole('USER')")
    public String user(){
        return "hello user";
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String hello(){
        return "hello everyone";
    }
}
