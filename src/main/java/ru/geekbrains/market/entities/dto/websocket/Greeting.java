package ru.geekbrains.market.entities.dto.websocket;

public class Greeting {

    private String content;

    public Greeting(){

    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}