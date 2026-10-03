package com.lifecare.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BedUnavailableException extends RuntimeException {
    public BedUnavailableException(String message) {
        super(message);
    }
}
