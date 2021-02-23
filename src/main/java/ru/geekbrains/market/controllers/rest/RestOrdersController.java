package ru.geekbrains.market.controllers.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.geekbrains.market.entities.Order;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.OrderService;
import ru.geekbrains.market.services.UserService;
import ru.geekbrains.market.utils.Cart;

import java.security.Principal;

@Controller
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class RestOrdersController {
    private UserService usersService;
    private OrderService ordersService;
    private Cart cart;

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder(Principal principal, @RequestParam String address) {
        User user = usersService.findByUserName(principal.getName()).get();
        Order order = new Order(user, cart, user.getUserName(), address);
        order = ordersService.saveOrder(order);
    }
}
