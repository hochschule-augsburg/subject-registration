package de.hochschule.augsburg.registration.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Registration {

    private final UUID id;

    private String student;

    private final List<SubjectSelection> subjectSelection;

    private UUID registrationWindowId;

    public void assignStudent(final String student) {
        this.student = student;
    }

    public void update(final RegistrationUpdate update) {
        this.subjectSelection.clear();
        this.subjectSelection.addAll(update.getSubjectSelection());
    }

    public void updateRegistrationWindowId(UUID registrationWindowId){
        this.registrationWindowId=registrationWindowId;
    }

}
