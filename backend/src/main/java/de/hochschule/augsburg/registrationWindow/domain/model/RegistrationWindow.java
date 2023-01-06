package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindow {
    private final UUID id;

    private final String semester;

    private String startDate;

    private String endDate;

    private RegistrationWindowStatus registrationWindowStatus;

    public void open() {
        this.registrationWindowStatus = RegistrationWindowStatus.active;
    }

    public void close() {
        this.registrationWindowStatus = RegistrationWindowStatus.closed;
    }

    public void update(final RegistrationWindowUpdate update) {
        this.startDate = update.getStartDate();
        this.endDate = update.getEndDate();
    }

}
