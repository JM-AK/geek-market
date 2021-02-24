package ru.geekbrains.market.controllers.rest;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.entities.dto.OrderItemDto;
import ru.geekbrains.market.services.OrderItemService;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.Cart;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
@Profile("js")
public class RestCartController {
    private OrderItemService orderItemService;
    private ProductService productService;
    private Cart cart;

    @GetMapping("/add/{productId}")
    public void addProductToCartById(@PathVariable Long productId) {
        Product product = productService.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Unable to add product (id = " + productId + " ) to cart. Product not found"));
        cart.add(product);
    }

    @GetMapping
    public List<OrderItemDto> getCartContent() {
        return orderItemService.mapEntityListToDtoList(cart.getItems());
    }

}
