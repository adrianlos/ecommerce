package com.github.skorczan.ecommerce.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    private String login;

    private String password;

    private Role role;

    private Address address;

    private String avatarUrl;

    private ContactPreference contactPreference;

    public enum Role {
        ADMIN,
        CUSTOMER
    }

    @Value
    @AllArgsConstructor
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
}
