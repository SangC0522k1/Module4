package com.bank.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class DataInputException extends RuntimeException{
    private static final Long serialVersionUID = 1L;
    public DataInputException(String message) {
        super(message);
    }
}
