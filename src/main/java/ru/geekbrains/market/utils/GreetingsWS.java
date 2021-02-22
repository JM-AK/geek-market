package ru.geekbrains.market.utils;

import ru.geekbrains.market.entities.websocket.Greeting;

public interface GreetingsWS {
    void sendMessage(String destination, Greeting message);
}
