package de.hochschule.augsburg.subject.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Subject {

    private final UUID id;

    private String name;

    private String professor;

    private Float creditPoints;

    private String description;

    private String specialization;

    private boolean status;
    private int capacity;



    public void assignProfessor(final String professor) {
        this.professor = professor;
    }

    public void update(final SubjectUpdate update) {
        this.name = update.getName();
        this.professor = update.getProfessor();
        this.creditPoints = update.getCreditPoints();
        this.description = update.getDescription();
        this.specialization = update.getSpecialization();
    }
}