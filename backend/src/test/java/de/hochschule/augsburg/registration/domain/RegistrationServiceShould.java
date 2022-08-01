package de.hochschule.augsburg.registration.domain;

import de.hochschule.augsburg.BaseServiceTest;
import de.hochschule.augsburg.registration.domain.mapper.RegistrationMapper;
import de.hochschule.augsburg.registration.domain.mapper.RegistrationMapperImpl;
import de.hochschule.augsburg.registration.domain.model.Registration;
import de.hochschule.augsburg.registration.domain.model.SubjectSelection;
import de.hochschule.augsburg.registration.domain.service.RegistrationService;
import de.hochschule.augsburg.registration.infrastructure.repository.RegistrationRepository;
import de.hochschule.augsburg.subject.domain.service.SubjectService;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles(profiles = {"test"})
@DisplayName("Registration Service Test")
@Import({RegistrationMapperImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationServiceShould extends BaseServiceTest {

    private RegistrationService registrationService;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Mock
    private SubjectService subjectService;

    @Mock
    private RuntimeService runtimeService;


    @BeforeEach
    public void initService() {
        this.registrationService = new RegistrationService(
                this.registrationRepository,
                this.registrationMapper,
                this.subjectService,
                this.runtimeService
        );
    }

    @Order(1)
    @Test
    @DisplayName("shouldCreateRegistration()")
    @Rollback(false)
    public void shouldCreateRegistration() {
        //input
        final List<SubjectSelection> subjectSelections = List.of(
                SubjectSelection.builder()
                        .subject(UUID.randomUUID())
                        .points(500)
                        .build());

        final Registration registration = Registration.builder()
                .student("max.mustermann")
                .subjectSelection(subjectSelections)
                .build();

        // Act
        this.registrationService.createRegistration(registration, "max.mustermann");

        final Registration saved = this.registrationService.getRegistrationByStudent("max.mustermann").orElseThrow();

        // Assert
        assertEquals(registration.getStudent(), saved.getStudent());
        assertEquals(registration.getSubjectSelection().size(), 1);

        //verfiy mocks
        verify(this.runtimeService, times(1)).startProcessInstanceByKey(anyString(), anyString(), anyMap());
    }

    @Order(2)
    @Test
    @DisplayName("shouldGetRegistrationByStudent()")
    public void shouldGetRegistrationByStudent() {

        // Act
        final Registration registration = this.registrationService.getRegistrationByStudent("max.mustermann").orElseThrow();

        // Assert
        assertEquals("max.mustermann", registration.getStudent());
    }

    @Order(2)
    @Test
    @DisplayName("shouldDeleteRegistration()")
    public void shouldDeleteRegistration() {

        // Act
        final Registration registration = this.registrationService.getRegistrationByStudent("max.mustermann").orElseThrow();
        this.registrationService.deleteRegistration(registration.getId(), "max.mustermann");

        // Assert
        assertTrue(this.registrationService.getRegistrationByStudent("max.mustermann").isEmpty());
    }

    @Order(2)
    @Test
    @DisplayName("shouldDeleteRegistration2()")
    public void shouldDeleteRegistration2() {

        // Act
        final Registration registration = this.registrationService.getRegistrationByStudent("max.mustermann").orElseThrow();
        this.registrationService.deleteRegistration(registration.getId(), "max.mustermann");

        // Assert
        assertTrue(this.registrationService.getRegistrationByStudent("max.mustermann").isEmpty());
    }

}
