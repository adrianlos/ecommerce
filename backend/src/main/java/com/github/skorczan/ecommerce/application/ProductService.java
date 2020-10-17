package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.api.CreateProductRequest;
import com.github.skorczan.ecommerce.domain.Product;
import com.github.skorczan.ecommerce.domain.ProductCategoryRepository;
import com.github.skorczan.ecommerce.domain.ProductRepository;
import com.github.skorczan.ecommerce.domain.ProductType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Links;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ProductDomainDtoConverter converter;

    private final ProductCategoryService categoryService;

    public Page<ProductDto> list(@NonNull Pageable page) {
        return repository.findAll(page).map(converter::convert);
    }

    public Optional<ProductDto> get(long id) {
        return repository.findById(id).map(converter::convert);
    }

    public ProductDto add(CreateProductRequest createProductRequest) {
        var product = converter.convert(createProductRequest);
        product = repository.save(product);
        return converter.convert(product);
    }

    public ProductDto add(ProductDto productDto) {
        var product = converter.convert(productDto);
        product = repository.save(product);
        return converter.convert(product);
    }

    public void remove(ProductDto productDto) {
        remove(productDto.getId());
    }

    public void remove(long productId) {
        repository.deleteById(productId);
    }

    public Page<ProductDto> findByAuthor(long productCategoryId, @NonNull Pageable page) {
        return repository.findByAuthorId(productCategoryId, page).map(converter::convert);
    }

    public Page<ProductDto> search(String name, String type, Long categoryId, Double minPrice, Double maxPrice, Pageable page) {
        return repository.search(
                Optional.ofNullable(name).map(String::toLowerCase).orElse(null),
                type != null ? ProductType.valueOf(type) : null,
                // TODO: don't pass all category ids when we are not looking for specific category id
                categoryService.list(categoryId).stream().map(ProductCategoryDto::getId).collect(Collectors.toUnmodifiableSet()),
                minPrice,
                maxPrice,
                page)
                .map(converter::convert);
    }
}
