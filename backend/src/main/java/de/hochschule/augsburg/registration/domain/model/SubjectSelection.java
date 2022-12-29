package de.hochschule.augsburg.registration.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SubjectSelection {

    public SubjectSelection(final SubjectSelectionUpdate update) {
        this.subject = update.getSubject();
        this.points = update.getPoints();
    }

    private UUID id;

    private final UUID subject;

    private final Integer points;

}
