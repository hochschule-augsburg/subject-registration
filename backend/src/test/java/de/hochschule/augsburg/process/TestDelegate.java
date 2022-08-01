package de.hochschule.augsburg.process;

import de.hochschule.augsburg.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestDelegate implements JavaDelegate {

    private final RegistrationService registrationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        //input -> Welche Variablen brauche ich
        final String test = delegateExecution.getVariable("fsad").toString();

        //processing -> Aufgabe ausführen
        registrationService.getAllRegistrations("");

        //output  -> schreibe etwas zurück wenn notwendig
        delegateExecution.setVariable("tet", "fadsfads");

    }
}
