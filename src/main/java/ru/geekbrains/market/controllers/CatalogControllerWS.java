package ru.geekbrains.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.market.entities.websocket.Greeting;
import ru.geekbrains.market.entities.websocket.Message;
import ru.geekbrains.market.utils.GreetingsWS;

@Controller
public class CatalogControllerWS implements GreetingsWS {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello_cart")
    @SendTo("/topic/add_product_to_cart")
    public Greeting greetingAddToCart(Message message) {
        return new Greeting(message.getName() + " добавлен в коризну!");
    }

    @MessageMapping("/hello_catalog")
    @SendTo("/topic/add_product_to_catalog")
    public Greeting greetingAddToCatalog(Message message) {
        return new Greeting(message.getName() + " добавлен в каталог!");
    }

    public void sendMessage(String destination, Greeting message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }

}
