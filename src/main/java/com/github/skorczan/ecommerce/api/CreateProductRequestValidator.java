package com.github.skorczan.ecommerce.api;

import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Set;

@Component
public final class CreateProductRequestValidator implements Validator {

    private static final Set<String> productTypes = Set.of("DOMESTIC", "FOOD", "CLOTHES", "SHOES");

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, CreateProductRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val product = (CreateProductRequest) o;

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

        if (product.getAuthorId() == null) {
            errors.rejectValue("authorId", "ABSENT");
        }

        if (product.getCategoryId() == null) {
            errors.rejectValue("categoryId", "ABSENT");
        }

        if (product.getType() == null) {
            errors.rejectValue("type", "ABSENT");
        } else if(!productTypes.contains(product.getType())){
            errors.rejectValue("type", "INVALID");
        }
    }
}
