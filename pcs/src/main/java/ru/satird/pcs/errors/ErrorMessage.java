package ru.satird.pcs.errors;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorMessage {

    private String title;
    private final int statusCode;
    private final Date timestamp;
    private final String message;
    private final String description;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

    public ErrorMessage(String title, int statusCode, Date timestamp, String message, String description) {
        this.title = title;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
