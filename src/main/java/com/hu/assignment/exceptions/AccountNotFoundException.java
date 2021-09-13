package com.hu.assignment.exceptions;

import org.iban4j.Iban;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(Iban iban) {
        super("Account with IBAN " + iban.toFormattedString() + " does not exist.");
    }
}
