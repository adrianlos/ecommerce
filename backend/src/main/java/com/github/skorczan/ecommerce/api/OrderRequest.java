package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.AddressDto;
import com.github.skorczan.ecommerce.application.OrderEntryDto;
import lombok.Value;
import java.util.List;


// TODO: check if Jackson works with getters returning Optional<Address>
@Value
public class OrderRequest {

    private AddressDto customerAddress;

    private AddressDto deliveryAddress;

    private List<Entry> entries;

    @Value
    public static class Entry {

        private long productId;

        private double count;
    }
}
