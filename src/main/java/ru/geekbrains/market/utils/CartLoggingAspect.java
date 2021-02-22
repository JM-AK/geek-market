package ru.geekbrains.market.utils;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CartLoggingAspect {

    @After("execution (public void ru.geekbrains.market.utils.Cart.*(..))")
    public void afterModifyInCartClass() {
        System.out.println("Test AOP Cart");
    }

    @After("execution * (public void ru.geekbrains.market.*.CartController.*(..))")
    public void afterModifyInCartControllerClass() {
        System.out.println("Test AOP CartController");
    }

}
