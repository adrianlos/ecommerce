package com.github.skorczan.ecommerce.application;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
public class UserDto {

    private Long id;

    private String login;

    private Role role;

    private Address address;

    private String avatarUrl;

    private ContactPreference contactPreference;

    public enum Role {
        ADMIN,
        CUSTOMER
    }

    @Value
    @Builder
    public static class Address {

        private String country;

        private String city;

        private String street;

        private String zipCode;
    }

    public enum ContactPreference {
        MAIL,
        EMAIL
    }

    @Builder
    private UserDto(@NonNull Long id,
                    @NonNull String login,
                    @NonNull Role role,
                    @NonNull String avatarUrl,
                    @NonNull ContactPreference contactPreference,
                    @NonNull String country,
                    @NonNull String city,
                    @NonNull String street,
                    @NonNull String zipCode) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.address = Address.builder()
                .country(country)
                .city(city)
                .street(street)
                .zipCode(zipCode)
                .build();
        this.avatarUrl = avatarUrl;
        this.contactPreference = contactPreference;
    }
}
