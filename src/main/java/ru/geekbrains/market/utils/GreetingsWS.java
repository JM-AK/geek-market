package ru.geekbrains.market.utils;

import ru.geekbrains.market.entities.dto.websocket.Greeting;

public interface GreetingsWS {
    void sendMessage(String destination, Greeting message);
}
