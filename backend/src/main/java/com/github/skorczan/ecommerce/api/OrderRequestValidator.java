package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public final class OrderRequestValidator implements Validator {

    private final AddressValidator addressValidator;

    private final ProductService productService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Objects.equals(aClass, OrderRequest.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        val request = (OrderRequest) o;

        errors.setNestedPath("customerAddress");
        addressValidator.validate(request.getCustomerAddress(), errors);

        errors.setNestedPath("deliveryAddress");
        addressValidator.validate(request.getDeliveryAddress(), errors);

        errors.setNestedPath("entries");

        for(int i = 0; i < request.getEntries().size(); ++i) {
            val entry = request.getEntries().get(i);

            if (productService.get(entry.getProductId()).isEmpty()) {
                errors.rejectValue(i + ".productId", "NOT_FOUND");
            }

            if (entry.getCount() <= 0) {
                errors.rejectValue(i + ".count", "MUST_BE_POSITIVE");
            } else if(entry.getCount() >= 1_000_000) {
                errors.rejectValue(i + ".count", "TOO_BIG");
            }
        }

        errors.setNestedPath("");
    }
}
