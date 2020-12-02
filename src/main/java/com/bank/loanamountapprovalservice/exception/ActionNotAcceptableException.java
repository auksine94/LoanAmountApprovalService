package com.bank.loanamountapprovalservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ActionNotAcceptableException extends RuntimeException {

    public ActionNotAcceptableException(String message) {
        super(message);
    }
}
