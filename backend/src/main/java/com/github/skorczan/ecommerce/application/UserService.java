package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.api.UserRegistrationRequest;
import com.github.skorczan.ecommerce.domain.User;
import com.github.skorczan.ecommerce.domain.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final UserDomainDtoConverter converter;

    private final PasswordEncoder passwordEncoder;

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
                .password(passwordEncoder.encode(request.getPassword()))
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
}
