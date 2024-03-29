package de.hochschule.augsburg.registrationWindow.domain.service;

import de.hochschule.augsburg.registrationWindow.domain.mapper.RegistrationWindowMapper;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindow;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowUpdate;
import de.hochschule.augsburg.registrationWindow.infrastructure.entity.RegistrationWindowEntity;
import de.hochschule.augsburg.registrationWindow.infrastructure.repository.RegistrationWindowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service to handle registration windows.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationWindowService {
    private final RegistrationWindowRepository registrationWindowRepository;
    private final RegistrationWindowMapper registrationWindowMapper;
    private final RuntimeService runtimeService;

    /**
     * Get all registration windows.
     *
     * @return registration windows
     */
    public List<RegistrationWindow> getAllRegistrationWindows() {
        return this.registrationWindowMapper.map(this.registrationWindowRepository.findAll());
    }

    public RegistrationWindow getActiveRegistrationWindow(){
        return this.registrationWindowMapper.map(this.registrationWindowRepository.findOpenRegistrationWindow());
    }

    /**
     * Create a new Registration Window.
     *
     * @param newRegistrationWindow Registration window that is created
     * @param professor             Professor that starts the new registration window
     * @return the new registration window
     */
    public RegistrationWindow createRegistrationWindow(final RegistrationWindow newRegistrationWindow, final String professor) {

        if (this.doesOpenRegistrationWindowNotExist()) {
            throw new RuntimeException("Registration window already open");
        }

        final VariableMap variables = Variables.createVariables();
        variables.putValue("registration_window_start", newRegistrationWindow.getStartDate());
        variables.putValue("registration_window_end", newRegistrationWindow.getEndDate());
        newRegistrationWindow.open();

        final RegistrationWindow savedRegistrationWindow = this.saveRegistrationWindow(newRegistrationWindow);

        this.runtimeService.startProcessInstanceByKey("Process_Registration_Window", savedRegistrationWindow.getId().toString(), variables);

        return savedRegistrationWindow;
    }

    /**
     * Update an existing registration window.
     *
     * @param registrationWindowUpdate Update that is applieded
     * @param professor                Id of the professor
     * @return the updated registrationWindow
     */
    public RegistrationWindow updateRegistrationWindow(final RegistrationWindowUpdate registrationWindowUpdate, final String professor) {
        final RegistrationWindow registrationWindow = this.getRegistrationWindow(registrationWindowUpdate.getId());

        // TODO is the registrationWindow of the professor with granted access?

        registrationWindow.update(registrationWindowUpdate);
        return this.saveRegistrationWindow(registrationWindow);
    }

    /**
     * Delete an existing registration window
     *
     * @param registrationWindowId
     * @param professor
     */
    public void deleteRegistrationWindow(final UUID registrationWindowId, final String professor) {
        final RegistrationWindow registrationWindow = this.getRegistrationWindow(registrationWindowId);

        // TODO is the registrationWindow of the professor with granted access?

        this.registrationWindowRepository.deleteById(registrationWindow.getId());
    }


    public void lockRegistrationWindow() {
        RegistrationWindowEntity registrationWindowEntity = this.registrationWindowRepository.findOpenRegistrationWindow();
        final RegistrationWindow registrationWindow = this.registrationWindowMapper.map(registrationWindowEntity);
        registrationWindow.close();
        registrationWindowEntity = this.registrationWindowMapper.map(registrationWindow);
        this.registrationWindowRepository.save(registrationWindowEntity);
    }

    // Helper Methods

    private RegistrationWindow saveRegistrationWindow(final RegistrationWindow registrationWindow) {
        final RegistrationWindowEntity savedRegistrationWindow = this.registrationWindowRepository.save(this.registrationWindowMapper.map(registrationWindow));
        return this.registrationWindowMapper.map(savedRegistrationWindow);
    }

    private RegistrationWindow getRegistrationWindow(final UUID registrationWindowId) {
        return this.registrationWindowRepository.findById(registrationWindowId)
                .map(this.registrationWindowMapper::map)
                .orElseThrow();
    }

    public boolean doesOpenRegistrationWindowNotExist() {
        return this.registrationWindowRepository.findOpenRegistrationWindow() == null;
    }

}
