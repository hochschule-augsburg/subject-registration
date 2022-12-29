package de.hochschule.augsburg.registration.api.delegates;

import de.hochschule.augsburg.registration.domain.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import javax.inject.Named;

@RequiredArgsConstructor
@Named
public class CloseRegistrationDelegate implements JavaDelegate {

    @Autowired
    private  RegistrationService service;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        final String registrationId = (String) delegateExecution.getVariable("registrationId");
        this.service.closeRegistration(UUID.fromString(registrationId));
    }
}
