package nl.hu.assignments.term1.week2.functionaltesting.data.entity;

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
public class Holder {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    private UUID id;
    private String bsn;
    private String name;
}
