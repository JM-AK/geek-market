package ru.geekbrains.market.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class GeekMarketError {
    private int status;
    private String message;
    private Date timestamp;

    public GeekMarketError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
