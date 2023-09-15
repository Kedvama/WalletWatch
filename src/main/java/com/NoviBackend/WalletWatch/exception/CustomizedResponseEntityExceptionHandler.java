package com.NoviBackend.WalletWatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex,
                                                           WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex,
                                                                      WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUniqueAlreadyExistsException(UniqueAlreadyExistsException ex,
                                                                           WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidLoginCredentials.class)
    public final ResponseEntity<Object> handleInvalidLoginCredentials(InvalidLoginCredentials ex,
                                                                      WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnableToSubscribeException.class)
    public final ResponseEntity<Object> handleUnableToSubscribeException(UnableToSubscribeException ex,
                                                                         WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(),
                        request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
