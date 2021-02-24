package ru.geekbrains.market.controllers.restjs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.geekbrains.market.entities.Order;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.DeliveryAddressService;
import ru.geekbrains.market.services.OrderService;
import ru.geekbrains.market.services.UserService;
import ru.geekbrains.market.beans.Cart;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/v1/orders")
@Profile("restjs")
@AllArgsConstructor
public class RestOrdersController {
    private UserService userService;
    private OrderService orderService;
    private DeliveryAddressService deliveryAddressService;
    private Cart cart;

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder(Principal principal, @RequestParam String address, @RequestParam String phone) {
        User user = userService.findByUserName(principal.getName()).get();
        Order order = orderService.makeOrder(cart, user);

        order.setDeliveryAddress(deliveryAddressService.getUserAddressOrCreateOne(user.getId(), address));
        order.setPhoneNumber(phone);
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(0.0);

        order = orderService.saveOrder(order);
    }
}
