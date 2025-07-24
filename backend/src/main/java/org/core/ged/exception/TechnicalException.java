package org.core.ged.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    public TechnicalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}