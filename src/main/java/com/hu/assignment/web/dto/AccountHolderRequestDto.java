package com.hu.assignment.web.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountHolderRequestDto {
    private Long version;
    @NotNull
    private String name;
}
