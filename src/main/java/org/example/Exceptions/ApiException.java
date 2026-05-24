package org.example.Exceptions;

import lombok.Getter;
import org.example.Enums.Status;

public class ApiException extends RuntimeException {
    @Getter
    private final Status status;


    public ApiException(Status status,String message) {
        super(message);
        this.status = status;
    }

    public Status getStatus() {
            return status;
    }
}
