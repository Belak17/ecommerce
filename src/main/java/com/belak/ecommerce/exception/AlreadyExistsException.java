package com.belak.ecommerce.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String mesage) {
        super(mesage);
    }
}
