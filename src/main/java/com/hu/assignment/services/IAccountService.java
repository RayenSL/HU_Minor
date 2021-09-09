package com.hu.assignment.services;

import com.hu.assignment.models.Account;

import java.util.Collection;

public interface IAccountService {

    public abstract void createAccount(Account account);

    public abstract void updateAccount(String id, Account account);

    public abstract void deleteAccount(String id);

    public abstract Collection<Account> getAccounts();

}
