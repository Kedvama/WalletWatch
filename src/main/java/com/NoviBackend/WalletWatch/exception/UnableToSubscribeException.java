package com.NoviBackend.WalletWatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UnableToSubscribeException extends RuntimeException {
    public UnableToSubscribeException(String message){
        super(message);
    }
}
