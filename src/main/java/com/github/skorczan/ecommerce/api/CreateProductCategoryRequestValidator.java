package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public final class CreateProductCategoryRequestValidator implements Validator {

    private final ProductCategoryService productCategoryService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, CreateProductCategoryRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val request = (CreateProductCategoryRequest) o;

        if (StringUtils.isEmpty(request.getName())) {
            errors.rejectValue("name", "EMPTY");
        } else if(request.getName().length() >= 255) {
            errors.rejectValue("name", "TOO_LONG");
        }

        if (!isParentCategoryIdValid(request.getParentCategoryId())) {
            errors.rejectValue("parentCategoryId", "UNKNOWN");
        }
    }

    private boolean isParentCategoryIdValid(Long categoryId) {
        return categoryId == null || productCategoryService.get(categoryId).isPresent();
    }
}
