package de.hochschule.augsburg.mailing.delegates;

import de.hochschule.augsburg.mailing.service.MailingService;
import de.hochschule.augsburg.mailing.utility.MailType;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@Named
public class RegistrationResultsMailing implements JavaDelegate {
    @Autowired
    MailingService mailingService;
    private final MailType mailType = MailType.REGISTARTION_RESULTS;
    private final String mailContent = "Sie haben die Fächer bekommen:/n";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //Here is DB request needed to get the list of Students,
        //but at the moment it's just a hardcoded email.


        //Here is also calculation mechanism needed to define, which courses got the student.
        //List<String> courses = getRegistartionResults(studentid);
        //for(String course : courses){
        //mailContent.append(course+"/n")
        // }
        mailingService.sendEmail(mailType, mailContent);
    }
}
