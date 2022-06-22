package com.example.demo.exceptions;

public class BalanceNotSufficentException extends RuntimeException {
    public BalanceNotSufficentException(String message) {
        super(message);
    }
}
