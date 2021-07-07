package ru.geekbrains.market.controllers.thymeleaf;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("thymeleaf")
public class LoginController {
    @GetMapping("/login")
    public String showMyLoginPage() {
        return "login";
    }
}
