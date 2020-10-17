package com.github.skorczan.ecommerce.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.val;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        getAuthentication(request).ifPresent(authentication ->
                SecurityContextHolder.getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }

    private Optional<Authentication> getAuthentication(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(TOKEN_HEADER))
                       .filter(tokenHeader -> tokenHeader.startsWith(TOKEN_PREFIX))
                       .map(tokenHeader -> tokenHeader.substring(TOKEN_PREFIX.length()))
                       .map(token -> {
                           try {
                               return JWT.require(Algorithm.HMAC256(secret))
                                       .build()
                                       .verify(token)
                                       .getSubject();
                           } catch (JWTVerificationException ex) {
                               logger.error("invalid token", ex);
                               return null;
                           }
                       })
                       .map(userDetailsService::loadUserByUsername)
                       .map(userDetails -> new UsernamePasswordAuthenticationToken(
                               userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));
    }
}
