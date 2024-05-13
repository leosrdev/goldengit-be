package com.goldengit.web.exception;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }

    public AccountAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
