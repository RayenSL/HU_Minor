package nl.hu.assignments.term1.week2.functionaltesting.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class HolderDto {
    private UUID id;
    private String name;
}
