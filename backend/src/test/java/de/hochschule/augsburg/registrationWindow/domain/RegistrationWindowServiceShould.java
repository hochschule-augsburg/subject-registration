package de.hochschule.augsburg.registrationWindow.domain;


import de.hochschule.augsburg.BaseServiceTest;
import de.hochschule.augsburg.registration.domain.model.Registration;
import de.hochschule.augsburg.registrationWindow.domain.mapper.RegistrationWindowMapper;
import de.hochschule.augsburg.registrationWindow.domain.mapper.RegistrationWindowMapperImpl;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindow;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowUpdate;
import de.hochschule.augsburg.registrationWindow.domain.service.RegistrationWindowService;
import de.hochschule.augsburg.registrationWindow.infrastructure.repository.RegistrationWindowRepository;
import de.hochschule.augsburg.subject.domain.mapper.SubjectMapperImpl;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(profiles = {"test"})
@DisplayName("RegistrationWindow Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({RegistrationWindowMapperImpl.class})
public class RegistrationWindowServiceShould extends BaseServiceTest {

    private RegistrationWindowService registrationWindowService;
    private final RegistrationWindow registrationWindow = RegistrationWindow.builder().semester("Wintersemester").startDate("2021-11-20T10:02:00").endDate("2021-20-20T10:02:00").build();
    private final String professor = "Max Mustermann";

    @Autowired
    private  RegistrationWindowRepository registrationWindowRepository;

    @Autowired
    private  RegistrationWindowMapper registrationWindowMapper;

    @Mock
    private RuntimeService runtimeService;

    @BeforeEach
    public void initService(){
        this.registrationWindowService = new RegistrationWindowService(this.registrationWindowRepository,this.registrationWindowMapper,this.runtimeService);

    }

    @Test
    public void shouldCreateRegistrationWindow(){

        this.registrationWindowService.createRegistrationWindow(registrationWindow,professor);

        RegistrationWindow saved = this.registrationWindowService.getAllRegistrationWindows().get(0);

        assertEquals(registrationWindow.getSemester(),saved.getSemester());
        assertEquals(registrationWindow.getStartDate(),saved.getStartDate());
        assertEquals(registrationWindow.getEndDate(),saved.getEndDate());


    }

    @Test
    public void shouldUpdateRegistrationWindow(){
        this.registrationWindowService.createRegistrationWindow(registrationWindow,professor);
        RegistrationWindow saved = this.registrationWindowService.getAllRegistrationWindows().get(0);
        RegistrationWindowUpdate registrationWindowUpdate =  RegistrationWindowUpdate.builder().id(saved.getId()).startDate("2021-11-20T10:02:00").endDate("2021-20-25T10:02:00").build();
        this.registrationWindowService.updateRegistrationWindow(registrationWindowUpdate,professor);

        RegistrationWindow updated = this.registrationWindowService.getAllRegistrationWindows().get(0);


        assertEquals(updated.getEndDate(),registrationWindowUpdate.getEndDate());


    }

    @Test
    public void shouldDeleteRegistrationWindow(){
        this.registrationWindowService.createRegistrationWindow(registrationWindow,professor);
        RegistrationWindow saved = this.registrationWindowService.getAllRegistrationWindows().get(0);
        this.registrationWindowService.deleteRegistrationWindow(saved.getId(),professor);
        assertTrue(this.registrationWindowService.getAllRegistrationWindows().isEmpty());
    }


}
