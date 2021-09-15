package com.hu.assignment.web.dto;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class AccountPatchDto {
    private Long version;
    @DecimalMin("1")
    private BigDecimal balance;
    private Boolean active;
}
