package com.hu.assignment.data.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(of = {"bsn"})
public class AccountHolder {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    private UUID id;
    private String bsn;
    private String name;
}