package de.hochschule.augsburg.registration.domain.service;

import de.hochschule.augsburg.registration.domain.mapper.RegistrationMapper;
import de.hochschule.augsburg.registration.domain.model.Registration;
import de.hochschule.augsburg.registration.domain.model.RegistrationStatus;
import de.hochschule.augsburg.registration.domain.model.RegistrationUpdate;
import de.hochschule.augsburg.registration.infrastructure.entity.RegistrationEntity;
import de.hochschule.augsburg.registration.infrastructure.repository.RegistrationRepository;
import de.hochschule.augsburg.registrationWindow.infrastructure.entity.RegistrationWindowEntity;
import de.hochschule.augsburg.registrationWindow.infrastructure.repository.RegistrationWindowRepository;
import de.hochschule.augsburg.subject.domain.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Service to handle registrations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationWindowRepository registrationWindowRepository;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final SubjectService subjectService;
    private final RuntimeService runtimeService;

    /**
     * Get all registrations.
     *
     * @return registrations
     */
    public List<Registration> getAllRegistrations(final String user) {


        // Todo Only dekan or admin should be able to request all registrations
        return this.registrationMapper.map(this.registrationRepository.findAll());
    }

    /**
     * Get registration by student.
     *
     * @param user Id of the student
     * @return registration
     */
    public Registration getRegistrationByStudent(final String user) {
        // Todo Check uid is equal to logged user, only admin and owner should be able to get registration
        return this.registrationMapper.map(this.registrationRepository.findByStudent(user));
    }

    /**
     * Create a new Registration.
     *
     * @param newRegistration Registration that is created
     * @param student         Student that gets assigned to the new registration
     * @return the new registration
     */
    public Registration createRegistration(final Registration newRegistration, final String student, final String mail) {
        // Todo only students and admin should be able to create a new registration

        final Registration existRegistration = this.getRegistrationByStudent(student);


        if (existRegistration != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration for " + student + " already exists");
        }

        //collect all UUIDs from Subject Selection and validate Subject
        newRegistration.getSubjectSelection().forEach(subjectSelection -> {
            this.subjectService.validateSubject(subjectSelection.getSubject());
        });
        RegistrationWindowEntity registrationWindow = registrationWindowRepository.findOpenRegistrationWindow();

        newRegistration.updateRegistrationWindowId(registrationWindow.getId());
        newRegistration.assignStudent(student);
        final Registration savedRegistration = this.saveRegistration(newRegistration);

        final VariableMap variables = Variables.createVariables();
        variables.put("registrationId", savedRegistration.getId().toString());
        variables.put("student", student);
        variables.put("mail", mail);

        // Start new instance with given attributes
        this.runtimeService.startProcessInstanceByKey("Process_Register_Subject", savedRegistration.getId().toString(), variables);

        return savedRegistration;
    }

    /**
     * Update an existing registration.
     *
     * @param registrationUpdate Update that is applied
     * @param student            ID of the student
     * @return the updated registration
     */
    public Registration updateRegistration(final RegistrationUpdate registrationUpdate, final String student) {
        final Registration registration = this.getRegistration(registrationUpdate.getId());


        //is the registration of the given student?
        if (!registration.getStudent().equals(student)) {
            throw new RuntimeException("Registration not available");
        }

        if (registration.getStatus() != RegistrationStatus.OPEN) {
            throw new RuntimeException("Registration already closed");
        }

        registration.update(registrationUpdate);
        return this.saveRegistration(registration);
    }


    public void deleteRegistration(final UUID registrationId, final String student) {
        // Todo only owner student and admin should be able to delete a registration
        final Registration registration = this.getRegistration(registrationId);

        //is the registration of the given student?
        if (!registration.getStudent().equals(student)) {
            throw new RuntimeException("Registration not available");
        }

        if (registration.getStatus() != RegistrationStatus.OPEN) {
            throw new RuntimeException("Registration already closed");
        }

        this.runtimeService.createMessageCorrelation("cancel_registration")
                .processInstanceBusinessKey(registration.getId().toString())
                .correlate();

        this.registrationRepository.deleteById(registration.getId());
    }

    public void closeRegistration(final UUID registrationId) {
        final Registration registration = this.getRegistration(registrationId);
        registration.close();
        this.registrationRepository.save(this.registrationMapper.map(registration));
    }

    // Helper Methods

    private Registration saveRegistration(final Registration registration) {
        final RegistrationEntity savedRegistration = this.registrationRepository.save(this.registrationMapper.map(registration));
        return this.registrationMapper.map(savedRegistration);
    }

    private Registration getRegistration(final UUID registrationId) {
        return this.registrationRepository.findById(registrationId)
                .map(this.registrationMapper::map)
                .orElseThrow();
    }

}
