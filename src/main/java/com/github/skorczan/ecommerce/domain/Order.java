package com.github.skorczan.ecommerce.domain;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer")
    private User customer;

    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "customer_country", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "customer_city", nullable = false)),
            @AttributeOverride(name = "street", column = @Column(name = "customer_street", nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "customer_zip_code", nullable = false))
    })
    private Address customerAddress;

    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "delivery_country", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city", nullable = false)),
            @AttributeOverride(name = "street", column = @Column(name = "delivery_street", nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_zip_code", nullable = false))
    })
    private Address deliveryAddress;

    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @ElementCollection
    @CollectionTable(name = "order_entries")
    private List<Entry> entries;

    public enum State {
        NEW,
        IN_DELIVERY,
        DELIVERED,
        CANCELLED
    }

    @Embeddable
    @Data
    public static class Entry {

        @ManyToOne(optional = false)
        @JoinColumn(name = "product")
        private Product product;

        @Column(name = "price")
        private double price;

        @Column(name = "count")
        private double count;
    }
}
