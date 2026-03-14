package com.belak.shoppingcart.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String mesage) {
        super(mesage);
    }
}
