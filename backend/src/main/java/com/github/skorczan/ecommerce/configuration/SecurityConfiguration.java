package com.github.skorczan.ecommerce.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skorczan.ecommerce.application.UserDetailsManagerImpl;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final AnonymousAuthenticationToken ANONYMOUS_CUSTOMER_TOKEN =
            new AnonymousAuthenticationToken("anonymous", "anonymous", Collections.singleton(new SimpleGrantedAuthority("CUSTOMER")));

    private final ObjectMapper objectMapper;

    private final String jwtSecret;

    private final long jwtExpirationTime;

    private final UserDetailsManager userDetailsManager;

    public SecurityConfiguration(ObjectMapper objectMapper,
                                 @Value("${jwt.secret}") String jwtSecret,
                                 @Value("${jwt.expirationTime}") long jwtExpirationTime,
                                 UserDetailsManager userDetailsManager) {
        this.objectMapper = objectMapper;
        this.jwtSecret = jwtSecret;
        this.jwtExpirationTime = jwtExpirationTime;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {
        authenticationManager.authenticationProvider(userCredentialsAuthenticationProvider())
                             .userDetailsService(userDetailsManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.anonymous()
            .authorities(List.copyOf(ANONYMOUS_CUSTOMER_TOKEN.getAuthorities()))
            .and()
            .authorizeRequests()
            .antMatchers("/", "/login").permitAll()
            .antMatchers(HttpMethod.GET, "/authors", "/authors/*").hasAnyAuthority("CUSTOMER", "ADMIN")
            .antMatchers(HttpMethod.OPTIONS, "/authors", "/authors/*").hasAnyAuthority("CUSTOMER", "ADMIN")
            .antMatchers("/authors/**").hasAuthority("ADMIN")
            .antMatchers("/orders/**").hasAuthority("CUSTOMER")
            .antMatchers(HttpMethod.GET, "/products/categories/**").hasAnyAuthority("CUSTOMER", "ADMIN")
            .antMatchers(HttpMethod.OPTIONS, "/products/categories/**").hasAnyAuthority("CUSTOMER", "ADMIN")
            .antMatchers("/products/categories/**").hasAuthority("ADMIN")
            .antMatchers(HttpMethod.GET, "/products/**").hasAnyAuthority("CUSTOMER", "ADMIN")
            .antMatchers(HttpMethod.OPTIONS, "/products/**").hasAnyAuthority("CUSTOMER", "ADMIN")
            .antMatchers("/products/**").hasAuthority("ADMIN")
            .antMatchers("/users/**").hasAuthority("ADMIN") // TODO: sing up and details about self
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(userCredentialsAuthenticationFilter())
            .addFilter(jwtAuthorizationFilter())
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN));
    }

    private UserCredentialsAuthenticationFilter userCredentialsAuthenticationFilter() throws Exception {
        val bean = new UserCredentialsAuthenticationFilter(objectMapper);
        bean.setAuthenticationManager(authenticationManager());
        bean.setAuthenticationSuccessHandler(new UserCredentialsAuthenticationSuccessHandler(jwtSecret, jwtExpirationTime));
        return bean;
    }

    private JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), userDetailsService(), jwtSecret);
    }

    private UserCredentialsAuthenticationProvider userCredentialsAuthenticationProvider() {
        return new UserCredentialsAuthenticationProvider(userDetailsService());
    }
}
