package ru.geekbrains.market.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class CartLoggingAspect {
    private Logger logger = Logger.getLogger(CartLoggingAspect.class.getName());

    //логирование класса Корзина - НЕ РАБОТАЕТ !!!
    @After("execution (public * ru.geekbrains.market.utils.Cart.*(..))")
    public void afterModifyInCartClass(JoinPoint jp) {
        logger.info(jp.toString());
    }

    //логирование работы с корзиной
    @After("execution (public * ru.geekbrains.market.*.CartController.*Product*(..))")
    public void afterModifyInCartControllerClass(JoinPoint jp) {
        MethodSignature ms = (MethodSignature) jp.getSignature();
        logger.info(ms.toString());
    }

}
