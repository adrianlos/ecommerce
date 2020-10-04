package com.github.skorczan.ecommerce.application;

import lombok.Builder;
import lombok.Value;


import java.time.LocalDateTime;
import java.util.List;


@Value
@Builder
public class OrderDto {

    private Long id;

    private long customer;

    private AddressDto customerAddress;

    private AddressDto deliveryAddress;

    private LocalDateTime orderTime;

    private OrderState state;

    private List<OrderEntryDto> entries;

}
