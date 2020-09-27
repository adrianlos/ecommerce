package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.ProductDto;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public final class ProductValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, ProductDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val product = (ProductDto) o;

        if (product.getId() != null) {
            errors.rejectValue("id", "PRESENT");
        }

        if (StringUtils.isEmpty(product.getTitle())) {
            errors.rejectValue("title", "EMPTY");
        } else if(product.getTitle().length() >= 512) {
            errors.rejectValue("title", "TOO_LONG");
        }

        if (StringUtils.isEmpty(product.getDescription())) {
            errors.rejectValue("description", "EMPTY");
        } else if(product.getDescription().length() >= 512) {
            errors.rejectValue("description", "TOO_LONG");
        }

        // TODO: validate URL
        if (StringUtils.isEmpty(product.getThumbnailUrl())) {
            errors.rejectValue("thumbnailUrl", "EMPTY");
        } else if(product.getThumbnailUrl().length() >= 512) {
            errors.rejectValue("thumbnailUrl", "TOO_LONG");
        }

        if (product.getPrice() == null) {
            errors.rejectValue("price", "ABSENT");
        }
    }
}
