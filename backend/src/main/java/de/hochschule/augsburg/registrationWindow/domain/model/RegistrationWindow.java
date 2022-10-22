package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindow {
    private final String id;

    private final String semester;

    private String startDate;

    private String endDate;

    private boolean status = true;

    public void update(final RegistrationWindowUpdate update) {
        this.startDate = update.getStartDate();
        this.endDate = update.getEndDate();
        this.status = update.isStatus();
    }
}
