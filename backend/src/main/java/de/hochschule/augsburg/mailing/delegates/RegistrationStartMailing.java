package de.hochschule.augsburg.mailing.delegates;

import javax.inject.Named;
import javax.persistence.Access;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import de.hochschule.augsburg.mailing.service.MailingService;
import de.hochschule.augsburg.mailing.utility.MailType;

@Named
public class RegistrationStartMailing implements JavaDelegate {
    @Autowired
    MailingService mailingService;
    private final MailType mailType = MailType.REGISTRATION_STARTED;
    private final String mailContent = "Registriere dich!";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //Here is DB request needed to get the list of Students,
        //but at the moment it's just a hardcoded email.
        String studentMail="musterman@gmail.com";
        mailingService.sendEmail(studentMail,mailType,mailContent);
    }
}
