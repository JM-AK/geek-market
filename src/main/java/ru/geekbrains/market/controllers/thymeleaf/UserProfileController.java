package ru.geekbrains.market.controllers.thymeleaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.market.entities.Order;
import ru.geekbrains.market.entities.SystemUser;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.OrderService;
import ru.geekbrains.market.services.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@Profile("thymeleaf")
public class UserProfileController {
    private UserServiceImpl userService;
    private OrderService orderService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping()
    public String getProfile (Model model, Principal principal){
        User user = userService.findByUserName(principal.getName()).get();

        List<Order> orders = orderService.findByUserId(user.getId());

        model.addAttribute("systemUser", user);
        model.addAttribute("orders", orders);
        return "profile-page";
    }

    @GetMapping("/orders/info/{id}")
    public String orderInfo(@PathVariable("id") Long id, Model model) throws Exception {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order-info-page";
    }
}
