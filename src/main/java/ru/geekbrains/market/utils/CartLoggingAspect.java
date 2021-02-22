package ru.geekbrains.market.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class CartLoggingAspect {
    private Logger logger = Logger.getLogger(CartLoggingAspect.class.getName());

    //логирование класса Корзина - НЕ РАБОТАЕТ !!!
    @After("execution (public void ru.geekbrains.market.utils.Cart.*(..))")
    public void afterModifyInCartClass(JoinPoint jp) {
        logger.info(jp.toString());
    }

    //логирование работы с корзиной
    @After("execution (public void ru.geekbrains.market.*.CartController.*(..))")
    public void afterModifyInCartControllerClass(JoinPoint jp) {
        logger.info(jp.toString());
    }

    //логирование метода добавления товара в корзину
    @After("execution (public * ru.geekbrains.market.*.CatalogController.*Cart(..))")
    public void afterModifyInCatalogControllerClassAddToCart(JoinPoint jp) {
        logger.info(jp.toString());
    }

}
