package de.hochschule.augsburg.subject.infrastructure.repository;

import de.hochschule.augsburg.subject.infrastructure.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface SubjectRepository extends JpaRepository<SubjectEntity, UUID> {

    SubjectEntity findByName(String name);

    Optional<SubjectEntity> findByIdAndAndProfessor(UUID id, String processor);

    void deleteSubjectByName(String name);
}
