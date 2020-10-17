package com.github.skorczan.ecommerce.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByAuthorId(long authorId, Pageable page);

    @Query("select p from Product p where " +
           "(:name is null or lower(p.title) like %:name%) and " +
           "(:type is null or p.type = :type) and " +
           "(p.category.id in :categoryIds) and " +
           "(:minPrice is null or p.price >= :minPrice) and " +
           "(:maxPrice is null or p.price <= :maxPrice)")
    Page<Product> search(@Param("name") String name,
                         @Param("type") ProductType type,
                         @Param("categoryIds") Set<Long> categoryIds,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice,
                         Pageable page);
}
