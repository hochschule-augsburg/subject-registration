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
public class SubjectSelectionUpdate {

    private final UUID subject;

    private final Integer points;

}
