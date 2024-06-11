package com.goldengit.domain.exception;

public class InvalidEmailDomainException extends Exception {

    public InvalidEmailDomainException(String message) {
        super(message);
    }

    public InvalidEmailDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
