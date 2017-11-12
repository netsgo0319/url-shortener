package com.example.shortener.exception;

public class NotFoundShortUrlKeyException extends RuntimeException {
    public NotFoundShortUrlKeyException() {
        super();
    }
    public NotFoundShortUrlKeyException(String message) {
        super(message);
    }
}
