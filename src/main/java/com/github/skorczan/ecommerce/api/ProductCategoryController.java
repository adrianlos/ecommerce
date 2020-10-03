package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.ProductCategoryDto;
import com.github.skorczan.ecommerce.application.ProductCategoryService;
import com.github.skorczan.ecommerce.application.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "products/categories", produces = "application/json")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    private final ProductService productService;

    private final CreateProductCategoryRequestValidator createProductCategoryRequestValidator;

    @GetMapping(path = "")
    public List<ProductCategoryDto> listProductCategories(@RequestParam(required = false) Long root) {
        return productCategoryService.list(root);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductCategoryDto> getProductCategory(@PathVariable long id) {
        return productCategoryService.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "", consumes = "application/json")
    public ResponseEntity<Errors> addProductCategory(@RequestBody CreateProductCategoryRequest request, Errors errors) {
        createProductCategoryRequestValidator.validate(request, errors);

        if(!errors.hasErrors()) {
            val author = productCategoryService.save(request.getName(), request.getParentCategoryId());
            val link = ControllerLinkBuilder.linkTo(methodOn(ProductCategoryController.class).getProductCategory(author.getId()));

            return ResponseEntity.created(link.toUri()).build();
        } else {
            // TODO: add errors to response
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(path = "/{productCategoryId}", consumes = "application/json")
    public ResponseEntity<Errors> removeProductCategory(@PathVariable long productCategoryId, @RequestParam(defaultValue="false") boolean wholeSubtree) {
        if (anyProductsRelated(productCategoryId)) {
            return ResponseEntity.badRequest().build();
        }

        if(wholeSubtree) {
            productCategoryService.removeAll(productCategoryId);
        } else {
            productCategoryService.removeOne(productCategoryId);
        }

        return ResponseEntity.noContent().build();
    }

    private boolean anyProductsRelated(long productCategoryId) {
        return !productService.findByAuthor(productCategoryId, PageRequest.of(0, 1)).isEmpty();
    }
}
