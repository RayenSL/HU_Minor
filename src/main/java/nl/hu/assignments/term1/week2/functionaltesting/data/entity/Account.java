package nl.hu.assignments.term1.week2.functionaltesting.data.entity;

import lombok.*;
import nl.hu.assignments.term1.week2.functionaltesting.data.IbanToStringConverter;
import org.hibernate.annotations.Type;
import org.iban4j.Iban;

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
    @Convert(converter = IbanToStringConverter.class)
    private Iban iban;
    private BigDecimal balance;
    @Singular
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "account_holder",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "holder_id"))
    private Set<Holder> accountHolders;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean active = true;

    public Set<Holder> getAccountHolders() {
        return Collections.unmodifiableSet(accountHolders);
    }

    public boolean addAccountHolder(Holder holder) {
        return accountHolders.add(holder);
    }

    public boolean removeAccountHolder(UUID accountHolderId) {
        return accountHolders.removeIf(holder -> holder.getId().equals(accountHolderId));
    }
}


