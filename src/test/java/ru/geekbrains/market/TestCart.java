package ru.geekbrains.market;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.geekbrains.market.beans.Cart;
import ru.geekbrains.market.entities.Product;

public class TestCart {
    Cart cart;
    Product p;

    @Before
    public void init() {
        System.out.println("init");
        cart = new Cart();
        p = new Product();
        p.setPrice(10.0);
        p.setTitle("Test product");
        p.setId(100L);
    }

    @Test
    public void testAdd() {
        cart.add(p);
        for (int i = 0; i < 10; i++){
            cart.increment(100L);
        }
        Assert.assertEquals(110.0,cart.getTotalPrice(),0.0);
    }

    @Test
    public void testRemove() {
        cart.add(p);
        cart.increment(100L);
        cart.remove(p);
        Assert.assertEquals(cart.getTotalPrice(),0.0, 0.0);

    }



}
