package de.hochschule.augsburg.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private final boolean ldapActive = false;

    public String getLoggedInUser() {
        if (ldapActive) {
            final Jwt auth = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return auth.getClaim("preferred_username");
        }else{
            return "tester";
        }
    }

    public String getRoleOfLoggedInUser() {
        if (ldapActive) {
            final Jwt auth = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return auth.getClaim("role");
        }else{
            return "tester";
        }
    }



}
