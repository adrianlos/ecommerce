package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.domain.ProductCategory;
import com.github.skorczan.ecommerce.domain.ProductCategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    private final ProductCategoryDomainDtoConverter converter;

    public Optional<ProductCategoryDto> get(long id) {
        return repository.findById(id).map(converter::convert);
    }

    public ProductCategoryDto save(@NonNull String name, Long parentCategoryId) {
        var entity = new ProductCategory();
        entity.setName(name);

        if(parentCategoryId != null) {
            repository.findById(parentCategoryId).ifPresent(entity::setParentCategory);
        } else {
            entity.setParentCategory(null);
        }

        entity = repository.save(entity);
        return converter.convert(entity);
    }

    public Optional<ProductCategoryDto> rename(long categoryId, String name) {
        return repository.findById(categoryId)
                .map(category -> {
                    category.setName(name);
                    return category;
                })
                .map(converter::convert);
    }

    public Optional<ProductCategoryDto>  changeParentCategory(long categoryId, Long parentCategoryId) {
        return repository.findById(categoryId)
                .map(category -> {
                    if(parentCategoryId != null) {
                        repository.findById(parentCategoryId).ifPresent(category::setParentCategory);
                    } else {
                        category.setParentCategory(null);
                    }
                    return category;
                })
                .map(converter::convert);
    }

    public void removeOne(long categoryId) {
        repository.findById(categoryId).ifPresent(category -> {
            for (final ProductCategory childCategory: category.getChildrenCategories()) {
                childCategory.setParentCategory(category.getParentCategory());
            }

            repository.delete(category);
        });
    }

    public void removeAll(long categoryId) {
        repository.findById(categoryId).ifPresent(category -> {
            Queue<ProductCategory> productCategoriesToProcess = new LinkedList<>();
            Deque<ProductCategory> productCategoriesToDelete = new LinkedList<>();

            productCategoriesToProcess.add(category);

            while (!productCategoriesToProcess.isEmpty()) {
                val productCategory = productCategoriesToProcess.poll();
                productCategoriesToDelete.addFirst(productCategory);
                productCategoriesToProcess.addAll(productCategory.getChildrenCategories());
            }

            while (!productCategoriesToDelete.isEmpty()) {
                repository.delete(productCategoriesToDelete.pollFirst());
            }
        });
    }

    // A -> B -> {C,D}
    // 1. removeAll(A)
    // 2. removeAll(B)
    // 3. removeAll(C)
    // 4. repository.delete(C);
    // 5. removeAll(D)
    // 6. repository.delete(D);
    // 7. repository.delete(B);
    // 8. repository.delete(A);
    @SuppressWarnings("unused") // for presentation purpose
    private void removeAll(ProductCategory category) {
        category.getChildrenCategories().forEach(this::removeAll);
        repository.delete(category);
    }

    public List<ProductCategoryDto> list(Long rootProductCategoryId) {
        List<ProductCategoryDto> result = new LinkedList<>();
        Queue<ProductCategory> queue = new LinkedList<>();

        queue.addAll(repository.findByParentCategoryId(rootProductCategoryId));

        while (!queue.isEmpty()) {
            val productCategory = queue.poll();
            result.add(converter.convert(productCategory));
            queue.addAll(productCategory.getChildrenCategories());
        }

        return result;
    }
}
