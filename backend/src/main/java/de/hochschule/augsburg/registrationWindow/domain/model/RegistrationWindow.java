package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindow {
    private final UUID id;

    private final String semester;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private RegistrationWindowStatus status;

    public void open() {
        this.status = RegistrationWindowStatus.active;
    }

    public void close() {
        this.status = RegistrationWindowStatus.closed;
    }

    public void update(final RegistrationWindowUpdate update) {
        this.startDate = update.getStartDate();
        this.endDate = update.getEndDate();
    }

}
