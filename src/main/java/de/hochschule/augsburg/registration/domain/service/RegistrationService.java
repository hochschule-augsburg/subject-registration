package de.hochschule.augsburg.registration.domain.service;

import de.hochschule.augsburg.registration.domain.mapper.RegistrationMapper;
import de.hochschule.augsburg.registration.domain.model.Registration;
import de.hochschule.augsburg.registration.domain.model.RegistrationUpdate;
import de.hochschule.augsburg.registration.domain.model.SubjectSelection;
import de.hochschule.augsburg.registration.domain.process.RegistrationProcessVariables;
import de.hochschule.augsburg.registration.infrastructure.entity.RegistrationEntity;
import de.hochschule.augsburg.registration.infrastructure.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.calendar.DateTimeUtil;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.h2.expression.Variable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/**
 * Service to handle registrations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ManagementService managementService;
    private final RegistrationProcessVariables registrationProcessVariables;

    /**
     * Get all registrations.
     *
     * @return registrations
     */
    public List<Registration> getAllRegistrations() {
        return this.registrationMapper.map(this.registrationRepository.findAll());
    }

    /**
     * Get registration by student.
     *
     * @param student Id of the student
     * @return registration
     */
    public Registration getRegistrationByStudent(final String student) {
        return this.registrationMapper.map(this.registrationRepository.findByStudent(student));
    }

    /**
     * Create a new Registration.
     *
     * @param newRegistration Registration that is created
     * @param student         Student that gets assigned to the new registration
     * @return the new registration
     */
    public Registration createRegistration(final Registration newRegistration, final String student) {

        VariableMap variables = Variables.createVariables();

        variables.put("anmeldefrist", registrationProcessVariables.getAnmeldefrist());
        variables.put("student", student);
        final Registration existRegistration = this.getRegistrationByStudent(student);


        //TODO student can only start one registration?
        if (existRegistration != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        newRegistration.assignStudent(student);
        final Registration savedRegistration = this.saveRegistration(newRegistration);

        this.runtimeService.startProcessInstanceByKey("Process_Register_Subject", savedRegistration.getId(), variables);

        return savedRegistration;
    }

    /**
     * Update an existing registration.
     *
     * @param registrationUpdate Update that is applieded
     * @param student            Id of the student
     * @return the updated registration
     */
    public Registration updateRegistration(final RegistrationUpdate registrationUpdate, final String student) {
        final Registration registration = this.getRegistration(registrationUpdate.getId());

        //is the registration of the given student?
        if (!registration.getStudent().equals(student)) {
            throw new RuntimeException("Registration not available");
        }

        registration.update(registrationUpdate);
        return this.saveRegistration(registration);
    }


    public void deleteRegistration(final String registrationId, final String student) {
        final Registration registration = this.getRegistration(registrationId);

        //is the registration of the given student?
        if (!registration.getStudent().equals(student)) {
            throw new RuntimeException("Registration not available");
        }

        this.registrationRepository.deleteById(registration.getId());
    }

    // Helper Methods

    private Registration saveRegistration(final Registration registration) {
        final RegistrationEntity savedRegistration = this.registrationRepository.save(this.registrationMapper.map(registration));
        return this.registrationMapper.map(savedRegistration);
    }

    private Registration getRegistration(final String registrationId) {
        return this.registrationRepository.findById(registrationId)
                .map(this.registrationMapper::map)
                .orElseThrow();
    }

}
