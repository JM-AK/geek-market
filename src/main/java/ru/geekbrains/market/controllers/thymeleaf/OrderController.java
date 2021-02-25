package ru.geekbrains.market.controllers.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.market.entities.DeliveryAddress;
import ru.geekbrains.market.entities.Order;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.DeliveryAddressService;
import ru.geekbrains.market.services.OrderService;
import ru.geekbrains.market.services.UserService;
import ru.geekbrains.market.beans.Cart;
import ru.geekbrains.market.utils.rabbitmq.CartReceiverRabbit;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
@Profile("thymeleaf")
public class OrderController {
    private OrderService orderService;
    private DeliveryAddressService deliverAddressService;
    private UserService userService;
    private CartReceiverRabbit cartReceiverRabbit;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setDeliverAddressService(DeliveryAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    @Autowired
    public void setCartReceiverRabbit(CartReceiverRabbit cartReceiverRabbit) {
        this.cartReceiverRabbit = cartReceiverRabbit;
    }

    @GetMapping
    public String firstRequest(Model model) {
        model.addAttribute("order", orderService.findAll());
        return "orders";
    }

    @GetMapping("/order/fill")
    public String orderFill(Model model, HttpServletRequest httpServletRequest, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName()).get();
        Cart cart = (Cart) httpServletRequest.getSession().getAttribute("cart");
        Order order = orderService.makeOrder(cart, user);

        try {
            cartReceiverRabbit.receiveProduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<DeliveryAddress> deliveryAddresses = deliverAddressService.getUserAddresses(user.getId());
        model.addAttribute("order", order);
        model.addAttribute("deliveryAddresses", deliveryAddresses);
        return "order-filler";
    }

    @PostMapping("/order/confirm")
    public String orderConfirm(Model model, HttpServletRequest httpServletRequest, @ModelAttribute(name = "order") Order orderFromFrontend, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUserName(principal.getName()).get();
        Cart cart = (Cart) httpServletRequest.getSession().getAttribute("cart");
        Order order = orderService.makeOrder(cart, user);
        order.setDeliveryAddress(orderFromFrontend.getDeliveryAddress());
        order.setPhoneNumber(orderFromFrontend.getPhoneNumber());
        order.setDeliveryDate(LocalDateTime.now().plusDays(7));
        order.setDeliveryPrice(0.0);
        order = orderService.saveOrder(order);
        model.addAttribute("order", order);
        return "order-filler";
    }

    @GetMapping("/order/result/{id}")
    public String orderConfirm(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // todo ждем до оплаты, проверка безопасности и проблема с повторной отправкой письма сделать одноразовый вход
        User user = userService.findByUserName(principal.getName()).get();
        Order confirmedOrder = orderService.findById(id);
        if (!user.getId().equals(confirmedOrder.getUser().getId())) {
            return "redirect:/";
        }
//        mailService.sendOrderMail(confirmedOrder);
        model.addAttribute("order", confirmedOrder);
        return "order-result";
    }

}
