package com.example.shortener.exception;

public class UnshortenableUrlException extends RuntimeException {
    public UnshortenableUrlException() {
        super();
    }
    public UnshortenableUrlException(String message) {
        super(message);
    }
}
