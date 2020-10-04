package com.github.skorczan.ecommerce.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Set<ProductCategory> findByParentCategoryId(Long parentCategoryId);
}
