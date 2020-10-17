package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.domain.User;
import com.github.skorczan.ecommerce.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails userDetails) {
        var user = User.builder()
                .login(userDetails.getUsername())
                .password(passwordEncoder.encode(userDetails.getPassword()))
                .role(findForUserDetails(userDetails))
                .contactPreference(User.ContactPreference.EMAIL)
                .avatarUrl("http://nothing")
                .build();

        repository.save(user);
    }

    private User.Role findForUserDetails(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(grantedAuthority -> {
                    switch (grantedAuthority.getAuthority()) {
                        case "ADMIN": return User.Role.ADMIN;
                        case "CUSTOMER": return User.Role.CUSTOMER;
                        default:
                            throw new IllegalStateException("unknown user role: " + grantedAuthority.getAuthority());
                    }
                })
                .reduce(User.Role.CUSTOMER, (firstRole, secondRole) ->
                        firstRole == User.Role.ADMIN || secondRole == User.Role.ADMIN ? User.Role.ADMIN : User.Role.CUSTOMER);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        throw new UnsupportedOperationException("maybe in future");
    }

    @Override
    public void deleteUser(String username) {
        repository.findByLogin(username).ifPresent(repository::delete);
    }

    @Override
    public void changePassword(String username, String password) {
        repository.findByLogin(username).ifPresent(user -> user.setPassword(passwordEncoder.encode(password)));
    }

    @Override
    public boolean userExists(String username) {
        return repository.findByLogin(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByLogin(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authoritiesFor(user)))
                .orElseThrow(() -> new UsernameNotFoundException("user " + username + " doesn't exist"));
    }

    private Collection<? extends GrantedAuthority> authoritiesFor(User user) {
        switch (user.getRole()) {
            case ADMIN:
                return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
            case CUSTOMER:
                return Collections.singleton(new SimpleGrantedAuthority("CUSTOMER"));
            default:
                throw new IllegalStateException("unknown role: " + user.getRole());
        }
    }
}
