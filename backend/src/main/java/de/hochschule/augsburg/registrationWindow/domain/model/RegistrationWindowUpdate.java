package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindowUpdate {
    private final UUID id;

    private final String startDate;

    private final String endDate;
}
