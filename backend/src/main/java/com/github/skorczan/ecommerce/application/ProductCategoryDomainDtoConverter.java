package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.domain.ProductCategory;
import com.github.skorczan.ecommerce.domain.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class ProductCategoryDomainDtoConverter {

    private final ProductCategoryRepository repository;

    public ProductCategoryDto convert(ProductCategory productCategory) {
        if (productCategory == null) {
            return null;
        }

        return ProductCategoryDto.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .parentCategoryId(Optional.ofNullable(productCategory.getParentCategory())
                        .map(ProductCategory::getId)
                        .orElse(null))
                .build();
    }

    public ProductCategory convert(ProductCategoryDto productCategory) {
        if (productCategory == null) {
            return null;
        }

        val result = new ProductCategory();
        result.setId(productCategory.getId());
        result.setName(productCategory.getName());
        repository.findById(productCategory.getParentCategoryId()).ifPresent(result::setParentCategory);
        return result;
    }
}
