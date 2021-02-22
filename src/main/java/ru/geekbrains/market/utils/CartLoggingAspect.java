package ru.geekbrains.market.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

@Aspect
@Component
public class CartLoggingAspect {
    private Logger logger = Logger.getLogger(CartLoggingAspect.class.getName());

    @Pointcut("execution (public * ru.geekbrains.market.utils.Cart.add(..))")
    public void afterAddInCartClass() {
    }

    @Pointcut("execution (public * ru.geekbrains.market.utils.Cart.remove(..))")
    public void afterRemoveInCartClass() {
    }

    @Pointcut("execution (public * ru.geekbrains.market.utils.Cart.increment(..))")
    public void afterIncrementInCartClass() {
    }

    @Pointcut("execution (public * ru.geekbrains.market.utils.Cart.increment(..))")
    public void afterDecrementInCartClass() {
    }

    @Pointcut("execution (public * ru.geekbrains.market.utils.Cart.setQuantity(..))")
    public void afterSetQInCartClass() {
    }

    @After("afterAddInCartClass() || afterRemoveInCartClass() || afterIncrementInCartClass() || afterDecrementInCartClass() || afterSetQInCartClass()")
    public void afterModifyInCartItemsInCartClass(JoinPoint jp) {
        logger.info(jp.toString());
    }

    @Before("execution (public * ru.geekbrains.market.*.CartController.*Product*(..))")
    public void afterModifyInCartControllerClass(JoinPoint jp) {
        MethodSignature ms = (MethodSignature) jp.getSignature();
        logger.info(ms.toString());
    }

}
