package org.training.onlinestoremanagementsystem.exception;

public class InvalidUser extends RuntimeException{

    private String errorMessage;

    public InvalidUser(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
