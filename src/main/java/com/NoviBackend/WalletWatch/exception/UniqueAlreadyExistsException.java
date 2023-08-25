package com.NoviBackend.WalletWatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UniqueAlreadyExistsException extends RuntimeException {
    public UniqueAlreadyExistsException(String message){
        super(message);
    }
}
