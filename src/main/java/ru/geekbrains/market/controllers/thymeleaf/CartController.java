package ru.geekbrains.market.controllers.thymeleaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.market.entities.Product;
import ru.geekbrains.market.entities.dto.websocket.Greeting;
import ru.geekbrains.market.exceptions.NotFoundException;
import ru.geekbrains.market.services.ProductService;
import ru.geekbrains.market.beans.Cart;
import ru.geekbrains.market.utils.rabbitmq.CartSenderRabbit;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/*
* Контроллер страницы управления корзиной товаров
* */

@Controller
@RequestMapping("/cart")
@Profile("thymeleaf")
public class CartController {
    private ProductService productService;
    private CatalogControllerWS catalogControllerWS;
    private Cart cart;
    private CartSenderRabbit cartSenderRabbit;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCatalogControllerWS(CatalogControllerWS catalogControllerWS) {
        this.catalogControllerWS = catalogControllerWS;
    }

    @Autowired
    public void setCartSenderRabbit(CartSenderRabbit cartSenderRabbit) {
        this.cartSenderRabbit = cartSenderRabbit;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @GetMapping
    public String showCartPage(Model model) {
        model.addAttribute("cart", cart);
        return "cart-page";
    }

    @GetMapping("/add/{product_id}")
    public String addProduct(@PathVariable(name = "product_id") Long productId, HttpServletRequest request) throws IOException, InterruptedException {
        Product p = productService.findById(productId).orElseThrow(() -> new NotFoundException());
        cart.add(p);

        String finalCount = String.valueOf(cart.getItems().size());
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catalogControllerWS.sendMessage("/topic/add_to_cart",
                    new Greeting("В корзине товаров: " + finalCount));
        }).start();

        try {
            cartSenderRabbit.sendProduct(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String referrer = request.getHeader("referer");
        return "redirect:" + referrer;
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
    public String setProductQuantity(@RequestParam(name = "product_id") Long productId,
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

//    public Cart getCurrentCart(HttpSession session) {
//        Cart cart = (Cart) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new Cart();
//            session.setAttribute("cart", cart);
//        }
//        return cart;
//    }


}
