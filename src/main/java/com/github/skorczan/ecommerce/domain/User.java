package com.github.skorczan.ecommerce.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private byte[] password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private Address address;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @Column(name = "contact_preference", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactPreference contactPreference;

    public enum Role {
        ADMIN,
        CUSTOMER
    }

    public enum ContactPreference {
        MAIL,
        EMAIL
    }

    @SuppressWarnings("unused")  // needed by Hibernate
    protected User() {
    }

    @Builder
    private User(Long id,
                 @NonNull String login,
                 @NonNull byte[] password,
                 @NonNull Role role,
                 @NonNull String avatarUrl,
                 @NonNull ContactPreference contactPreference,
                 @NonNull String country,
                 @NonNull String city,
                 @NonNull String street,
                 @NonNull String zipCode) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.address = new Address();
        this.address.setCountry(country);
        this.address.setCity(city);
        this.address.setStreet(street);
        this.address.setZipCode(zipCode);
        this.avatarUrl = avatarUrl;
        this.contactPreference = contactPreference;
    }
}
