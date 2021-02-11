package ru.geekbrains.market.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;


/*
* Контроллер страницы управления корзиной товаров
* */

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private ProductService productService;
    private Cart cart;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping
    public String showCartPage(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return "cart";
    }

    @GetMapping("/add/{product_id}")
    public void addToCart(
            @PathVariable(name = "product_id") Long productId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product p = productService.findById(productId).orElseThrow(
                () -> new NotFoundException());
        cart.add(p);
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/inc/{product_id}")
    public String incrementProduct(@PathVariable(name = "product_id") Long productId) {
        cart.increment(productId);
        return "redirect:/cart";
    }

    @GetMapping("/dec/{product_id}")
    public String decrementProduct(@PathVariable(name = "product_id") Long productId) {
        cart.decrement(productId);
        return "redirect:/cart";
    }

    @GetMapping()
    public String setProductQuantity(Model model,
                                     @RequestParam(name = "product_id") Long productId,
                                     @RequestParam(name = "product_quantity") Long quantity) {
        Product p = productService.findById(productId).orElseThrow(
                () -> new NotFoundException());
        cart.setQuantity(p,quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{product_id}")
    public String removeProduct(@PathVariable(name = "product_id") Long productId) {
        cart.remove(productId);
        return "redirect:/cart";
    }






}
