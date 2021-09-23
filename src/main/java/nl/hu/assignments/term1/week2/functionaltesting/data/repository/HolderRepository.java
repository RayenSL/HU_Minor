package nl.hu.assignments.term1.week2.functionaltesting.data.repository;

import nl.hu.assignments.term1.week2.functionaltesting.data.entity.Holder;
import org.iban4j.Iban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID> {
    @Query("select distinct h from Account a left join a.accountHolders h where a.iban = ?1")
    List<Holder> findAllByAccountIban(Iban iban);
}
