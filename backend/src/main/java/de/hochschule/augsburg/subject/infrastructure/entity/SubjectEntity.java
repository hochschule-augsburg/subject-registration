package de.hochschule.augsburg.subject.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "hsa_subject")
@Table(name = "hsa_subject" )
public class SubjectEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "professor", nullable = false)
    private String professor;

    @Column(name = "cp", nullable = false)
    private Float creditPoints;

    @Column(name= "description", nullable = true, length = 4096)
    private String description;

    @Column(name = "specialization", nullable = true)
    private String specialization;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;
}
