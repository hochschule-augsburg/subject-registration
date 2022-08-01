package de.hochschule.augsburg.subject.domain;


import de.hochschule.augsburg.BaseServiceTest;
import de.hochschule.augsburg.registration.domain.mapper.RegistrationMapperImpl;
import de.hochschule.augsburg.subject.domain.mapper.SubjectMapper;
import de.hochschule.augsburg.subject.domain.mapper.SubjectMapperImpl;
import de.hochschule.augsburg.subject.domain.model.Subject;
import de.hochschule.augsburg.subject.domain.model.SubjectUpdate;
import de.hochschule.augsburg.subject.domain.service.SubjectService;
import de.hochschule.augsburg.subject.infrastructure.repository.SubjectRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles(profiles = {"test"})
@DisplayName("Subject Service Test")
@Import({SubjectMapperImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubjectServiceShould extends BaseServiceTest {

    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectMapper subjectMapper;

    @BeforeEach
    public void initService(){
        this.subjectService= new SubjectService(this.subjectRepository,this.subjectMapper);
    }


    @Order(1)
    @Test
    @Rollback(false)
    public void shouldCreateSubject(){
       final Subject subject = Subject.builder().name("Java II").creditPoints(5f).description("Example description").specialization("Software Engineering").status(true).capacity(50).build();

       this.subjectService.createSubject(subject,"Max Mustermann");

       final Subject saved = this.subjectService.getAllSubjects().get(0);

       assertEquals(subject.getProfessor(),saved.getProfessor());
       assertEquals(subject.getDescription(),saved.getDescription());

       assertEquals(subject.getCapacity(), saved.getCapacity());

    }

    @Order(2)
    @Test
    public void shouldDeleteSubject(){
        final Subject saved = this.subjectService.getAllSubjects().get(0);
        this.subjectService.deleteSubject(saved.getName(),saved.getProfessor());
        assertTrue(!this.subjectService.getAllSubjects().contains(saved));

    }

    @Order(3)
    @Test
    @Rollback(true)
    public void shouldUpdateSubject(){
        final Subject subject = Subject.builder().name("Java II").creditPoints(5f).description("Example description").specialization("Software Engineering").status(true).capacity(50).build();

        this.subjectService.createSubject(subject,"Max Mustermann");

        Subject saved = this.subjectService.getAllSubjects().get(0);

        final SubjectUpdate subjectUpdate = SubjectUpdate.builder().id(saved.getId()).name("Java II").creditPoints(7.5f).description("Example description").specialization("Software Engineering").status(true).capacity(50).professor("Max Mustermann").build();

        this.subjectService.updateSubject(subjectUpdate,"Max Mustermann");

        Subject updated = this.subjectService.getAllSubjects().get(0);

        assertEquals(updated.getCreditPoints(),subjectUpdate.getCreditPoints());

    }

    @Order(4)
    @Test
    public void shouldCreateSubjects(){
        subjectService.deleteAllSubjects();
        final Subject subject = Subject.builder().name("Java II").creditPoints(5f).description("Example description").specialization("Software Engineering").status(true).capacity(50).build();
        final Subject subject2 = Subject.builder().name("Mathe I").creditPoints(5f).description("Example description").specialization("Science").status(true).capacity(30).build();
        final Subject subject3 = Subject.builder().name("Mathe II").creditPoints(5f).description("Example description").specialization("Science").status(true).capacity(20).build();
        this.subjectService.createSubject(subject,"Max Mustermann");
        this.subjectService.createSubject(subject2,"Max Mustermann");
        this.subjectService.createSubject(subject3,"Max Mustermann");

        List<Subject> subjects = this.subjectService.getAllSubjects();

        assertEquals(subjects.size(),3);


    }




}
