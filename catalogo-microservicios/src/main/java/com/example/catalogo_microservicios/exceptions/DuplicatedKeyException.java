package com.example.catalogo_microservicios.exceptions;

public class DuplicatedKeyException extends Exception {
    private static final long serialVersionUID = 1L;
    private final static String MESSAGE_STRING = "Duplicate key";

    public DuplicatedKeyException() {
        this(MESSAGE_STRING);
    }

    public DuplicatedKeyException(String message) {
        super(message);
    }

    public DuplicatedKeyException(Throwable cause) {
        this(MESSAGE_STRING, cause);
    }

    public DuplicatedKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedKeyException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
