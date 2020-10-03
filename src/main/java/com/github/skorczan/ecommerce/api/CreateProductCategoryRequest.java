package com.github.skorczan.ecommerce.api;

import lombok.Value;

@Value
public class CreateProductCategoryRequest {

    private String name;

    private Long parentCategoryId;
}
