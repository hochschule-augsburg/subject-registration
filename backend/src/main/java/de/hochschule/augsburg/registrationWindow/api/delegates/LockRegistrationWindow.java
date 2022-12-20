package de.hochschule.augsburg.registrationWindow.api.delegates;

import de.hochschule.augsburg.registrationWindow.domain.service.RegistrationWindowService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class LockRegistrationWindow implements JavaDelegate {
    private final RegistrationWindowService registrationWindowService;

    @Override
    public void execute(final DelegateExecution delegateExecution) throws Exception {
        this.registrationWindowService.lockRegistrationWindow();
    }
}
