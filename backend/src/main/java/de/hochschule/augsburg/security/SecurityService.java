package de.hochschule.augsburg.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
public class SecurityService {

    private final boolean ldapActive = true;

    public String getLoggedInUser() {
        if (ldapActive) {
            final Jwt auth = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return auth.getClaim("preferred_username");
        } else {
            return "tester";
        }
    }

    public String getUserEmail() {
        if (ldapActive) {
            final Jwt auth = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return auth.getClaim("email");
        } else {
            return "tester";
        }
    }

    public String getStudent() {
        if (ldapActive) {
            final Jwt auth = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return auth.getClaim("name");
        } else {
            return "tester";
        }
    }

    public boolean isAdmin() {
        final Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority("ROLE_wpf-admin"));
    }

    public boolean isWindow() {
        final Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority("ROLE_wpf-window"));
    }

    public boolean isStudent() {
        final List<String> roles = asList("ROLE_hsa-stud");
        final List<SimpleGrantedAuthority> studentAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();

        return CollectionUtils.containsAny(authorities, studentAuthorities);
    }

    public boolean isProfessor() {
        final List<String> roles = asList("ROLE_professoren", "ROLE_lehrbeauftr");
        final List<SimpleGrantedAuthority> studentAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        final Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();

        return CollectionUtils.containsAny(authorities, studentAuthorities);
    }
}
