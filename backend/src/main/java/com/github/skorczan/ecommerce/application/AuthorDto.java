package com.github.skorczan.ecommerce.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthorDto {

    private Long id;

    private String firstName;

    private String lastName;
}
