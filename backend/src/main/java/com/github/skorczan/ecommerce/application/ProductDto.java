package com.github.skorczan.ecommerce.application;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDto {

    private Long id;

    private String title;

    private String description;

    private String thumbnailUrl;

    private Double price;

    private String type;

    private AuthorDto author;

    private ProductCategoryDto category;
}
