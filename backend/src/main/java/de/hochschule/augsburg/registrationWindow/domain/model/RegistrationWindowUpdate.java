package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindowUpdate {
    private final UUID id;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    private final RegistrationWindowStatus status;
}
