package ru.geekbrains.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.geekbrains.market.entities.websocket.Greeting;
import ru.geekbrains.market.entities.websocket.Message;
import ru.geekbrains.market.utils.GreetingsWS;

@Controller
public class CatalogControllerWS implements GreetingsWS {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(3000); // simulated delay
        return new Greeting(message.getName() + " добавлен в коризну!");
    }

    public void sendMessage(String destination, Greeting message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }

}
