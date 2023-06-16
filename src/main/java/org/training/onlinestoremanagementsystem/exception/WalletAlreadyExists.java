package org.training.onlinestoremanagementsystem.exception;

import lombok.Data;

@Data
public class WalletAlreadyExists extends RuntimeException{

    public final String errorMessage;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public WalletAlreadyExists(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
