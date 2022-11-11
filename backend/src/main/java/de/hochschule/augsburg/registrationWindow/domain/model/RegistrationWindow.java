package de.hochschule.augsburg.registrationWindow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegistrationWindow {
    private final String id;

    private final String semester;

    private String startDate;

    private String endDate;

    private RegistrationWindowStatus registrationWindowStatus;

    public void open(){
        this.registrationWindowStatus = RegistrationWindowStatus.ACTIVE;
    }
    public void close(){
        this.registrationWindowStatus = RegistrationWindowStatus.CLOSED;
    }
    public void update(final RegistrationWindowUpdate update) {
        this.startDate = update.getStartDate();
        this.endDate = update.getEndDate();
    }

}
