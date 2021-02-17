package ru.geekbrains.market.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.entities.websocket.Greeting;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.utils.Cart;
import ru.geekbrains.market.utils.GreetingsWS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/*
* Контроллер страницы управления корзиной товаров
* */

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private ProductService productService;
    private GreetingsWS controllerWs;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setControllerWs(GreetingsWS controllerWs) {
        this.controllerWs = controllerWs;
    }

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping
    public String showCartPage(HttpSession session, Model model) {
        Cart cart = getCurrentCart(session);
        model.addAttribute("cart", cart);
        return "cart-page";
    }

    @GetMapping("/add/{product_id}")
    public String addToCart(@PathVariable(name = "product_id") Long productId, HttpServletRequest request) throws IOException {
        Product p = productService.findById(productId).orElseThrow(() -> new NotFoundException());
        Cart cart = getCurrentCart(request.getSession());
        cart.add(p);
        String referrer = request.getHeader("referer");

        String finalCount = String.valueOf(cart.getItems().size());
//        logger.info(finalCount);
        controllerWs.sendMessage("/topic/greetings", new Greeting(finalCount));

        return "redirect:" + referrer;
    }

    @GetMapping("/inc/{product_id}")
    public String incrementProduct(@PathVariable(name = "product_id") Long productId, HttpSession session) {
        Cart cart = getCurrentCart(session);
        cart.increment(productId);
        return "redirect:/cart";
    }

    @GetMapping("/dec/{product_id}")
    public String decrementProduct(@PathVariable(name = "product_id") Long productId, HttpSession session) {
        Cart cart = getCurrentCart(session);
        cart.decrement(productId);
        return "redirect:/cart";
    }

    //ToDo fix address
    @GetMapping("/set/{product_id}")
    public String setProductQuantity(Model model,
                                     @RequestParam(name = "product_id") Long productId,
                                     @RequestParam(name = "product_quantity") Long quantity,
                                     HttpSession session) {
        Product p = productService.findById(productId).orElseThrow(
                () -> new NotFoundException());
        Cart cart = getCurrentCart(session);
        cart.setQuantity(p,quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{product_id}")
    public String removeProduct(@PathVariable(name = "product_id") Long productId, HttpSession session) {
        Cart cart = getCurrentCart(session);
        cart.remove(productId);
        return "redirect:/cart";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("not found");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    public Cart getCurrentCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }


}
