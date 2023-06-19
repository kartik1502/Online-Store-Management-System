package org.training.onlinestoremanagementsystem.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashSet;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.bad_request}")
    private String errorCodeBadRequest;

    @Value("${spring.application.conflict}")
    private String errorCodeConflict;

    @Value("${spring.application.not_acceptable}")
    private String errorCodeNotAcceptable;

    @Value("${spring.application.not_found}")
    private String errorCodeNotFound;

    @Value("${spring.application.un_authorized}")
    private String errorCodeUnauthorized;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Set<String> errors = new HashSet<>();
        for (ObjectError error: ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        ErrorResponse response = new ErrorResponse(errorCodeBadRequest, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExists ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeConflict, Set.of(ex.getLocalizedMessage())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordDoseNotMatch.class)
    public ResponseEntity<Object> handlePasswordDoseNotMatch(PasswordDoseNotMatch ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotAcceptable, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchUserExists.class)
    public ResponseEntity<Object> handleNoSuchUserExists(NoSuchUserExists ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotFound, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentials ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeUnauthorized, Set.of(ex.getLocalizedMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidUser.class)
    public ResponseEntity<Object> handleInvalidUser(InvalidUser ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotAcceptable, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchProductExists.class)
    public ResponseEntity<Object> handleNoSuchProductExists(NoSuchProductExists ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotFound, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuantityExceeded.class)
    public ResponseEntity<Object> handleQuantityExceeded(QuantityExceeded ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotAcceptable, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchCartExists.class)
    public ResponseEntity<Object> handleNoSuchCartExists(NoSuchCartExists ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotFound, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchWalletExists.class)
    public ResponseEntity<Object> handleNoSuchWalletExists(NoSuchWalletExists ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotFound, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WalletAlreadyExists.class)
    public ResponseEntity<Object> handleWalletAlreadyExists(WalletAlreadyExists ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeConflict, Set.of(ex.getLocalizedMessage())), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InSufficientWalletBalance.class)
    public ResponseEntity<Object> handleInSufficientWalletBalance(InSufficientWalletBalance ex) {
        return new ResponseEntity<>(new ErrorResponse(errorCodeNotAcceptable, Set.of(ex.getLocalizedMessage())), HttpStatus.NOT_ACCEPTABLE);
    }
}
