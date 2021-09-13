package com.hu.assignment.data.repository;

import com.hu.assignment.data.entity.Account;
import org.iban4j.Iban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByIban(Iban iban);
}
