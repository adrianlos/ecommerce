package com.github.skorczan.ecommerce.configuration;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class UserCredentialsAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        val username = authentication.getName();
        val requestedAuthorities = authentication.getAuthorities();
        val actualAuthorities = userDetailsService.loadUserByUsername(username).getAuthorities();

        if (actualAuthorities.containsAll(requestedAuthorities)) {
            return authentication;
        } else {
            throw new BadCredentialsException("insufficient privileges");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
