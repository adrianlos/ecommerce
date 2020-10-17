package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.api.CreateProductRequest;
import com.github.skorczan.ecommerce.domain.AuthorRepository;
import com.github.skorczan.ecommerce.domain.Product;
import com.github.skorczan.ecommerce.domain.ProductCategoryRepository;
import com.github.skorczan.ecommerce.domain.ProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductDomainDtoConverter {

    private final AuthorDomainDtoConverter authorDomainDtoConverter;

    private final ProductCategoryDomainDtoConverter productCategoryConverter;

    private final AuthorRepository authorRepository;

    private final ProductCategoryRepository productCategoryRepository;

    public ProductDto convert(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .author(authorDomainDtoConverter.convert(product.getAuthor()))
                .category(productCategoryConverter.convert(product.getCategory()))
                .type(product.getType().name())
                .build();
    }

    public Product convert(ProductDto product) {
        if (product == null) {
            return null;
        }

        return Product.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .author(authorDomainDtoConverter.convert(product.getAuthor()))
                .category(productCategoryConverter.convert(product.getCategory()))
                .type(ProductType.valueOf(product.getType()))
                .build();
    }

    public Product convert(CreateProductRequest product) {
        if (product == null) {
            return null;
        }

        return Product.builder()
                .title(product.getTitle())
                .description(product.getDescription())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .author(authorRepository.findById(product.getAuthorId()).orElseThrow())
                .category(productCategoryRepository.findById(product.getCategoryId()).orElseThrow())
                .type(ProductType.valueOf(product.getType()))
                .build();
    }
}
