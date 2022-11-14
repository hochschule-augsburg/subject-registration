package de.hochschule.augsburg.mailing.utility;

public enum MailType {
    REGISTRATION_STARTS_SOON("Anmeldung startet bald"),
    REGISTRATION_STARTED("Anmeldung geht los"),
    REGISTARTION_RESULTS("Ergebnisse der Anmeldung");
    private final String mailType;

    private MailType(String mailType){
        this.mailType =mailType;
    }

    @Override
    public String toString() {
        return mailType;
    }
}
