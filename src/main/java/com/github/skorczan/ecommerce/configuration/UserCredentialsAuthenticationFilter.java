package com.github.skorczan.ecommerce.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skorczan.ecommerce.api.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserCredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try (val reader = request.getReader()) {
            val payload = reader.lines().collect(Collectors.joining("\n"));
            val credentials = objectMapper.readValue(payload, UserCredentials.class);
            val token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
            setDetails(request, token);
            return getAuthenticationManager().authenticate(token);
        } catch (IOException ex) {
            throw new UncheckedIOException("can't parse authentication request", ex);
        }
    }
}
