package nl.hu.assignments.term1.week2.functionaltesting.domain.exception;

import org.iban4j.Iban;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account blocked")
public class AccountBlockedException extends Throwable {
    public AccountBlockedException(Iban iban) {
        super("Account with IBAN + " + iban.toFormattedString() + " is blocked.");
    }
}
