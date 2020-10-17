package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.domain.Address;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
class AddressDomainDtoConverter {

    public AddressDto convert(Address address) {
        if (address == null) {
            return null;
        }

        return new AddressDto(address.getCountry(), address.getCity(),
                address.getStreet(), address.getZipCode());
    }

    public Address convert(AddressDto address) {
        if (address == null) {
            return null;
        }

        val result = new Address();
        result.setCountry(address.getCountry());
        result.setCity(address.getCity());
        result.setStreet(address.getStreet());
        result.setZipCode(address.getZipCode());
        return result;
    }
}
