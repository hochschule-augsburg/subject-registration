package de.hochschule.augsburg.registrationWindow.domain.model;

public enum RegistrationWindowStatus {
    ACTIVE("active"),
    CLOSED("closed");
    private String value;
    RegistrationWindowStatus(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
