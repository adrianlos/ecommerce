package com.github.skorczan.ecommerce.application;

import lombok.Value;

@Value
public class OrderEntryDto {

    private long productId;

    private double price;

    private double count;
}
