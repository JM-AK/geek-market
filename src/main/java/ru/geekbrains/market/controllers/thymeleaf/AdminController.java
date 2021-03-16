package ru.geekbrains.market.controllers.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.market.entities.Order;
import ru.geekbrains.market.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Profile("thymeleaf")
public class AdminController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showAdminDashboard() {
        return "admin-panel";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders-page";
    }

    @GetMapping("/orders/ready/{id}")
    public void orderReady(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Order order = orderService.findById(id);
        orderService.changeOrderStatus(order, 2L);
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/orders/info/{id}")
    public String orderInfo(@PathVariable("id") Long id, Model model) throws Exception {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order-info-page";
    }



}
