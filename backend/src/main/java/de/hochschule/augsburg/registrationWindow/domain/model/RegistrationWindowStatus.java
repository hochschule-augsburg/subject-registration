package de.hochschule.augsburg.registrationWindow.domain.model;

public enum RegistrationWindowStatus {
    active("active"),
    closed("closed");
    private String value;

    RegistrationWindowStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
