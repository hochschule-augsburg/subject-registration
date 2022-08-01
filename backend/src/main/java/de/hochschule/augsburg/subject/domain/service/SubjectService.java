package de.hochschule.augsburg.subject.domain.service;

import de.hochschule.augsburg.subject.domain.mapper.SubjectMapper;
import de.hochschule.augsburg.subject.domain.model.Subject;
import de.hochschule.augsburg.subject.domain.model.SubjectUpdate;
import de.hochschule.augsburg.subject.infrastructure.entity.SubjectEntity;
import de.hochschule.augsburg.subject.infrastructure.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    /**
     * Get all subjects.
     *
     * @return subjects
     */
    public List<Subject> getAllSubjects() {
        return this.subjectMapper.map(this.subjectRepository.findAll());
    }

    /**
     * Create a new subject
     *
     * @param newSubject subject that is created
     * @param professor  Professor that gets assigned to the new subject
     * @return the new subject
     */
    public Subject createSubject(final Subject newSubject, final String professor) {
        newSubject.assignProfessor(professor);

        return this.saveSubject(newSubject);
    }

    /**
     * Delete subject
     *
     * @param subjectName Name of the subject
     * @param professor   ID of the Professor
     */
    public void deleteSubject(final String subjectName, final String professor) {
        final Subject subject = this.subjectMapper.map(this.subjectRepository.findByName(subjectName));

        //is the subject of the given professor?
        if (!subject.getProfessor().equals(professor)) {
            throw new RuntimeException("Subject is not available for delete");
        }

        this.subjectRepository.deleteSubjectByName(subjectName);
    }

    public void deleteAllSubjects() {
        this.subjectRepository.deleteAll();
    }

    /**
     * Update an existing subject.
     *
     * @param subjectUpdate Update that is applied
     * @param professor     ID of the professor
     * @return the updated subject
     */
    public Subject updateSubject(final SubjectUpdate subjectUpdate, final String professor) {

        final Subject subject = this.getSubject(subjectUpdate.getId());

        //is the subject of the given professor?
        if (!subject.getProfessor().equals(professor) && professor.equals("admin")) {
            throw new RuntimeException("Update not allowed for " + professor);
        }

        subject.update(subjectUpdate);
        return this.saveSubject(subject);
    }

    /**
     * validate subject.
     *
     * @param subjectId Subject ID to validate
     */
    public void validateSubject(final UUID subjectId) {
        if (this.findSubject(subjectId) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "subject: " + subjectId + " not exists!");
        }
    }

    // Helper Methods

    private Subject saveSubject(final Subject subject) {
        final SubjectEntity savedSubject = this.subjectRepository.save(this.subjectMapper.map(subject));
        return this.subjectMapper.map(savedSubject);
    }

    private Subject getSubject(final UUID subjectId) {
        return this.subjectRepository.findById(subjectId)
                .map(this.subjectMapper::map)
                .orElse(null);
    }

    private Subject findSubject(final UUID subjectId) {
        return this.subjectRepository.findById(subjectId)
                .map(this.subjectMapper::map)
                .orElse(null);
    }

    public boolean hasAccess(final String userId, final UUID subjectId) {
        return this.subjectRepository.findByIdAndAndProfessor(subjectId, userId).isPresent();
    }
}
