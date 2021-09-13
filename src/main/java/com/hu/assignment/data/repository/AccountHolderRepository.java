package com.hu.assignment.data.repository;

import com.hu.assignment.data.entity.AccountHolder;
import org.iban4j.Iban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, UUID> {

    Optional<AccountHolder> findByBsn(String bsn);

    @Query("select distinct h from Account a left join a.accountHolders h where a.iban = ?1")
    List<AccountHolder> findAllByAccountIban(Iban iban);
}
