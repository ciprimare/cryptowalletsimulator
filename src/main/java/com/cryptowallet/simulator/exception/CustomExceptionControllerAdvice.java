package com.cryptowallet.simulator.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
class CustomExceptionControllerAdvice {

    @ResponseBody
    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    CustomExceptionResponse walletNotFoundHandler(WalletNotFoundException ex) {
        return new CustomExceptionResponse(new Date(), ex.getMessage(), null);
    }

    @ResponseBody
    @ExceptionHandler(DuplicateWalletException.class)
    @ResponseStatus(BAD_REQUEST)
    CustomExceptionResponse duplicateWalletHandler(DuplicateWalletException ex) {
        return new CustomExceptionResponse(new Date(), ex.getMessage(), null);
    }

    @ResponseBody
    @ExceptionHandler(WalletCurrencyNotSupportedException.class)
    @ResponseStatus(BAD_REQUEST)
    CustomExceptionResponse walletCurrencyNotSupportedHandler(WalletCurrencyNotSupportedException ex) {
        return new CustomExceptionResponse(new Date(), ex.getMessage(), null);
    }
}
