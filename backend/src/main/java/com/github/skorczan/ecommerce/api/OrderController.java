package com.github.skorczan.ecommerce.api;

import com.github.skorczan.ecommerce.application.OrderDto;
import com.github.skorczan.ecommerce.application.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "orders", produces = "application/json")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderRequestValidator orderRequestValidator;

    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long id) {
        return orderService.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "", consumes = "application/json")
    public ResponseEntity<Errors> makeOrder(@RequestBody OrderRequest request, Errors errors) {
        orderRequestValidator.validate(request, errors);

        if(!errors.hasErrors()) {
            val author = orderService.makeOrder(0L, request);
            val link = ControllerLinkBuilder.linkTo(methodOn(OrderController.class).getOrder(author.getId()));

            return ResponseEntity.created(link.toUri()).build();
        } else {
            // TODO: add errors to response
            return ResponseEntity.badRequest().build();
        }
    }
}
