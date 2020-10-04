package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.domain.Order;
import com.github.skorczan.ecommerce.domain.ProductRepository;
import com.github.skorczan.ecommerce.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class OrderDomainDtoConverter {

    private final AddressDomainDtoConverter addressConverter;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public OrderDto convert(Order order) {
        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .id(order.getId())
                .customer(order.getCustomer().getId())
                .customerAddress(addressConverter.convert(order.getCustomerAddress()))
                .deliveryAddress(addressConverter.convert(order.getDeliveryAddress()))
                .entries(order.getEntries()
                              .stream()
                              .map(entry -> new OrderEntryDto(entry.getProduct().getId(), entry.getPrice(), entry.getCount()))
                              .collect(Collectors.toUnmodifiableList()))
                .orderTime(order.getOrderTime())
                .state(OrderState.valueOf(order.getState().name()))
                .build();
    }

    public Order convert(OrderDto order) {
        if (order == null) {
            return null;
        }

        val result = new Order();
        result.setId(order.getId());
        result.setCustomer(userRepository.findById(order.getCustomer()).orElse(null));
        result.setCustomerAddress(addressConverter.convert(order.getCustomerAddress()));
        result.setDeliveryAddress(addressConverter.convert(order.getDeliveryAddress()));
        result.setOrderTime(order.getOrderTime());
        result.setState(Order.State.valueOf(order.getState().name()));
        result.setTotalCost(order.getEntries().stream().mapToDouble(entry -> entry.getPrice() * entry.getCount()).sum());
        result.setEntries(order.getEntries()
                .stream()
                .map(entry -> {
                    val domainEntry = new Order.Entry();
                    domainEntry.setProduct(productRepository.findById(entry.getProductId()).orElse(null));
                    domainEntry.setPrice(entry.getPrice());
                    domainEntry.setCount(entry.getCount());
                    return domainEntry;
                })
                .collect(Collectors.toUnmodifiableList()));
        return result;
    }
}
