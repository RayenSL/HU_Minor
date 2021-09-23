package nl.hu.assignments.term1.week2.functionaltesting.domain.exception;

import org.iban4j.Iban;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(Iban iban) {
        super("Account with IBAN " + iban.toFormattedString() + " does not exist.");
    }
}
