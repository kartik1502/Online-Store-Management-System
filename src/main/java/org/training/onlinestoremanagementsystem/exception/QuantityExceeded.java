package org.training.onlinestoremanagementsystem.exception;

import lombok.Data;

@Data
public class QuantityExceeded extends RuntimeException{

    private final String errorMessage;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public QuantityExceeded(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
