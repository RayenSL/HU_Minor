package com.hu.assignment.exceptions;

import org.iban4j.Iban;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account already blocked")
public class AccountAlreadyBlockedException extends Exception {
    public AccountAlreadyBlockedException(Iban iban) {
        super("Account with IBAN + " + iban.toFormattedString() + " is already blocked.");
    }
}
