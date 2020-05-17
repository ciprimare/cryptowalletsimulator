package com.cryptowallet.simulator.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
class NotFoundExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    String walletNotFoundHandler(WalletNotFoundException ex) {
        return ex.getMessage();
    }

}
