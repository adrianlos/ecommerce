package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.api.UserRegistrationRequest;
import com.github.skorczan.ecommerce.domain.User;
import com.github.skorczan.ecommerce.domain.UserRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    private final UserDomainDtoConverter converter;

    private final MessageDigest messageDigest;

    public UserService(UserRepository repository, UserDomainDtoConverter converter) {
        this.repository = repository;
        this.converter = converter;

        try {
            this.messageDigest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("algorithm must be defined", ex);
        }
    }

    public Page<UserDto> list(@NonNull Pageable page) {
        return repository.findAll(page).map(converter::convert);
    }

    public Optional<UserDto> get(long userId) {
        return repository.findById(userId).map(converter::convert);
    }

    public Optional<UserDto> get(String login) {
        return repository.findByLogin(login).map(converter::convert);
    }

    public UserDto register(UserRegistrationRequest request) {
        repository.findByLogin(request.getLogin()).ifPresent(user -> {
            throw new IllegalArgumentException("user already exists");
        });

        var user = User.builder()
                .login(request.getLogin())
                .password(hashPassword(request.getPassword()))
                .role(User.Role.valueOf(request.getRole().name()))
                .contactPreference(User.ContactPreference.valueOf(request.getContactPreference().name()))
                .avatarUrl(request.getAvatarUrl())
                .country(request.getAddress().getCountry())
                .city(request.getAddress().getCity())
                .street(request.getAddress().getStreet())
                .zipCode(request.getAddress().getZipCode())
                .build();

        user = repository.save(user);
        return converter.convert(user);
    }

    // TODO: where is the salt?
    private byte[] hashPassword(String password) {
        return messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}
