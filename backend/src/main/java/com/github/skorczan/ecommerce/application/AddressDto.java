package com.github.skorczan.ecommerce.application;

import lombok.Value;

@Value
public class AddressDto {

    private String country;

    private String city;

    private String street;

    private String zipCode;
}
