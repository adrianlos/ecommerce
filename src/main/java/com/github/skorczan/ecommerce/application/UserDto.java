package com.github.skorczan.ecommerce.application;


import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
public class UserDto {

    private Long id;

    private String login;

    private Role role;

    private AddressDto address;

    private String avatarUrl;

    private ContactPreference contactPreference;

    public enum Role {
        ADMIN,
        CUSTOMER
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
        this.address = new AddressDto(country, city, street, zipCode);
        this.avatarUrl = avatarUrl;
        this.contactPreference = contactPreference;
    }
}
