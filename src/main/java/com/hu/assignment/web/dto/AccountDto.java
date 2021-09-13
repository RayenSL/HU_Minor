package com.hu.assignment.web.dto;
import com.sun.istack.NotNull;
import lombok.*;
import org.iban4j.Iban;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"iban"})
public class AccountDto {
    private Iban iban;
    @NotNull
    private BigDecimal balance;
    private boolean active = true;
}
