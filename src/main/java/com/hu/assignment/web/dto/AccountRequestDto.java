package com.hu.assignment.web.dto;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountRequestDto {
    private Long version;
    @NotNull
    @DecimalMin("1")
    private BigDecimal balance;
}