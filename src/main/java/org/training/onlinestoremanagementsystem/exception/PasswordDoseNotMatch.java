package org.training.onlinestoremanagementsystem.exception;

public class PasswordDoseNotMatch extends RuntimeException{
    private final String errorMessage;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public PasswordDoseNotMatch(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
