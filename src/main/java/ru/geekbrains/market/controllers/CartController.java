package ru.geekbrains.market.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.Cart;

import javax.servlet.http.HttpSession;


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

    //ToDo fix address
    @GetMapping("/set/{product_id}")
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

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("not found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }




}
