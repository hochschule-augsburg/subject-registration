package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindowUpdate {
    private final String id;

    private final String startDate;

    private final String endDate;

    private final boolean status;
}
