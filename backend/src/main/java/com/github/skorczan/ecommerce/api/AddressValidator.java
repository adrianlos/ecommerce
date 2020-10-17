package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.AddressDto;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class AddressValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, AddressDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val address = (AddressDto) o;

        if (StringUtils.isEmpty(address.getCountry())) {
            errors.rejectValue("country", "EMPTY");
        } else if(address.getCountry().length() >= 64) {
            errors.rejectValue("country", "TOO_LONG");
        }

        if (StringUtils.isEmpty(address.getCity())) {
            errors.rejectValue("city", "EMPTY");
        } else if(address.getCity().length() >= 64) {
            errors.rejectValue("city", "TOO_LONG");
        }

        if (StringUtils.isEmpty(address.getStreet())) {
            errors.rejectValue("street", "EMPTY");
        } else if(address.getStreet().length() >= 64) {
            errors.rejectValue("street", "TOO_LONG");
        }

        if (StringUtils.isEmpty(address.getZipCode())) {
            errors.rejectValue("zipCode", "EMPTY");
        } else if(address.getZipCode().length() >= 16) {
            errors.rejectValue("zipCode", "TOO_LONG");
        }
    }
}
