package com.goldengit.web.exception;

public class DisposableEmailException extends Exception {

    public DisposableEmailException(String message) {
        super(message);
    }

    public DisposableEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
