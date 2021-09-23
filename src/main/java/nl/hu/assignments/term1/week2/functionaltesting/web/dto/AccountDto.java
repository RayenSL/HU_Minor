package nl.hu.assignments.term1.week2.functionaltesting.web.dto;

import lombok.*;
import org.iban4j.Iban;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"iban"})
public class AccountDto {
    private Iban iban;
    @NotNull
    @DecimalMin("1")
    private BigDecimal balance;
    private boolean active = true;
}
