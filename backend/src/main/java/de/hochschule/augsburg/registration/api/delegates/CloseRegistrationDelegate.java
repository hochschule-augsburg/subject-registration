package de.hochschule.augsburg.registration.api.delegates;

import de.hochschule.augsburg.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CloseRegistrationDelegate implements JavaDelegate {

    private final RegistrationService service;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        final String registrationId = (String) delegateExecution.getVariable("registrationId");
        this.service.closeRegistration(UUID.fromString(registrationId));
    }
}
