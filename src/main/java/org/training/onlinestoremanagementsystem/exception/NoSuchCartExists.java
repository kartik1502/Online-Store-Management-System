package org.training.onlinestoremanagementsystem.exception;

import lombok.Data;

@Data
public class NoSuchCartExists extends RuntimeException {

    private final String errorMessage;

    public NoSuchCartExists(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
