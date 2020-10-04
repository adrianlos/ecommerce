package com.github.skorczan.ecommerce.api;

import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Set;

@Component
public final class AddAuthorValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, AddAuthor.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val product = (AddAuthor) o;

        if (StringUtils.isEmpty(product.getFirstName())) {
            errors.rejectValue("firstName", "EMPTY");
        } else if(product.getFirstName().length() >= 255) {
            errors.rejectValue("firstName", "TOO_LONG");
        }

        if (StringUtils.isEmpty(product.getLastName())) {
            errors.rejectValue("lastName", "EMPTY");
        } else if(product.getLastName().length() >= 255) {
            errors.rejectValue("lastName", "TOO_LONG");
        }
    }
}
