package de.hochschule.augsburg.registration.domain.delegates;

import javax.inject.Named;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import de.hochschule.augsburg.registrationWindow.domain.mapper.RegistrationWindowMapper;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindow;
import de.hochschule.augsburg.registrationWindow.infrastructure.entity.RegistrationWindowEntity;
import de.hochschule.augsburg.registrationWindow.infrastructure.repository.RegistrationWindowRepository;

@Named
@RequiredArgsConstructor
public class LockRegistrationWindow implements JavaDelegate {
    RegistrationWindowRepository registrationWindowRepository;
    private final RegistrationWindowMapper registrationWindowMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        RegistrationWindowEntity registrationWindowEntity =registrationWindowRepository.findOpenRegistrationWindow();
        RegistrationWindow registrationWindow=registrationWindowMapper.map(registrationWindowEntity);
        registrationWindow.close();
        registrationWindowEntity = registrationWindowMapper.map(registrationWindow);
        registrationWindowRepository.save(registrationWindowEntity);
    }
}
