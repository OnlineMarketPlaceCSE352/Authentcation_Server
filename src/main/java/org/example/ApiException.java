package org.example;

import org.example.Enums.Status;

import java.security.MessageDigest;

public class ApiException extends RuntimeException {
    private final Status status;


    public ApiException(Status status,String message) {
        super(message);
        this.status = status;
    }

    public Status getStatus() {
            return status;
    }
}
