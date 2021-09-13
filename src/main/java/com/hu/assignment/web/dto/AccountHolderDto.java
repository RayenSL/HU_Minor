package com.hu.assignment.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class AccountHolderDto {
    private String bsn;
    private String name;
}
