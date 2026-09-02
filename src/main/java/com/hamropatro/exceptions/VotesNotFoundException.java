package com.hamropatro.exceptions;

public class VotesNotFoundException extends RuntimeException {
    public VotesNotFoundException(String message) {
        super(message);
    }
}
