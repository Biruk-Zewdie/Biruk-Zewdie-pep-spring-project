package com.example.exception;

public class duplicateAccountException extends RuntimeException {

    public duplicateAccountException(String message) {
        super (message);
    }

}
