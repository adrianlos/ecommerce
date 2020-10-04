package com.github.skorczan.ecommerce.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnailUrl", nullable = false)
    private String thumbnailUrl;

    @OneToOne(optional = false)
    @JoinColumn(name = "category")
    private ProductCategory category;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author")
    private Author author;

    @SuppressWarnings("unused")  // needed by Hibernate
    protected Product() {
    }

    @Builder
    private Product(Long id, String title, String description, String thumbnailUrl, ProductCategory category, double price, ProductType type, Author author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.price = price;
        this.type = type;
        this.author = author;
    }
}
