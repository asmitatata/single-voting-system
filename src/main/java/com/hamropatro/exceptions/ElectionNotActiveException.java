package com.hamropatro.exceptions;

public class ElectionNotActiveException extends RuntimeException {
    public ElectionNotActiveException(String message) {
        super(message);
    }
}
