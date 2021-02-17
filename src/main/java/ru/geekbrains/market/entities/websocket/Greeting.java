package ru.geekbrains.market.entities.websocket;

public class Greeting {

    private int id;

    private String content;

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}