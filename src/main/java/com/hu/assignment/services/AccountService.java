package com.hu.assignment.services;

import com.hu.assignment.data.entity.Account;
import com.hu.assignment.data.entity.AccountHolder;
import com.hu.assignment.data.repository.AccountHolderRepository;
import com.hu.assignment.data.repository.AccountRepository;
import com.hu.assignment.exceptions.AccountAlreadyBlockedException;
import com.hu.assignment.exceptions.AccountBlockedException;
import com.hu.assignment.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.iban4j.Iban;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountHolderRepository holderRepository;

    public Page<Account> findAll(Pageable page) {
        return accountRepository.findAll(page);
    }

    public Account save(Account accountToSave) {
        return accountRepository.save(accountToSave);
    }

    public Account update(Iban iban, AccountDto accountDto) throws AccountNotFoundException, AccountAlreadyBlockedException {
        var account = this.getAccountFromIban(iban);

        var tryingToBlockAlreadyBlockedAccount = !account.isActive() && !accountDto.isActive();
        if (tryingToBlockAlreadyBlockedAccount) {
            throw new AccountAlreadyBlockedException(iban);
        }

        account = account.toBuilder()
                .active(accountDto.isActive())
                .build();

        return accountRepository.save(account);
    }

    public List<AccountHolder> findAllHoldersByIban(Iban iban) {
        return holderRepository.findAllByAccountIban(iban);
    }

    public Account addHolder(Iban iban, AccountHolder holder) throws AccountNotFoundException, AccountBlockedException {
        var account = this.getAccountFromIban(iban);

        if (!account.isActive()) {
            throw new AccountBlockedException(iban);
        }

        account.addAccountHolder(holder);

        return accountRepository.save(account);
    }

    public Account removeHolder(Iban accountIban, AccountHolder holder) throws AccountNotFoundException {
        final var account = this.getAccountFromIban(accountIban);

        account.removeAccountHolder(holder.getId());

        return accountRepository.save(account);
    }

    private Account getAccountFromIban(Iban iban) throws AccountNotFoundException {
        final var optionalAccount = accountRepository.findByIban(iban);
        return optionalAccount.orElseThrow(() -> new AccountNotFoundException(iban.toString()));
    }

}
