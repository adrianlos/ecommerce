package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.AddressDto;
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
}
