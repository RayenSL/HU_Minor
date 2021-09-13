package com.hu.assignment.data.entity;

import com.hu.assignment.data.IbanConverter;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode(of = {"iban"})
@ToString
public class Account {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    private UUID id;
    @Convert(converter = IbanConverter.class)
    private BigDecimal balance;
    @Singular
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "account_holder",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "holder_id"))
    private Set<AccountHolder> accountHolders;
    private boolean active = true;

    public Set<AccountHolder> getAccountHolders() {
        return Collections.unmodifiableSet(accountHolders);
    }

    public boolean addAccountHolder(AccountHolder holder) {
        return accountHolders.add(holder);
    }

    public boolean removeAccountHolder(UUID accountHolderId) {
        return accountHolders.removeIf(holder -> holder.getId().equals(accountHolderId));
    }
}
