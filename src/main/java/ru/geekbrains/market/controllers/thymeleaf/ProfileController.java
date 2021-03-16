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
import ru.geekbrains.market.services.UserService;
import ru.geekbrains.market.services.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@Profile("thymeleaf")
public class ProfileController {
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

    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/edit/{id}")
    public String updateUserDetails (@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest, Principal principal){
        User user = userService.findById(id).get();
        List<Order> orders = orderService.findByUserId(id);

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "profile-page";
    }

    @PostMapping("/edit/{user_id}")
    public String updateUserDetails(@Valid @ModelAttribute("systemUser") User userDTO, BindingResult theBindingResult, Model theModel) {
        String userName = userDTO.getUserName();
        if (theBindingResult.hasErrors()) {
            return "profile-page";
        }

        User user = userService.findByUserName(userName).get();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        userService.update(user);
        logger.debug("Successfully updated user: " + userName);
        return "profile-page";
    }
}
