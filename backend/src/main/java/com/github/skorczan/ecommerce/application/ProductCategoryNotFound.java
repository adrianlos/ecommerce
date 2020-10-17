package com.github.skorczan.ecommerce.application;

import lombok.Value;

import javax.persistence.EntityNotFoundException;

@Value
public final class ProductCategoryNotFound extends EntityNotFoundException {

    private long productCategoryId;

    public ProductCategoryNotFound(long productCategoryId) {
        super("product category " + productCategoryId + " not found");
        this.productCategoryId = productCategoryId;
    }
}
