package de.hochschule.augsburg.registration.infrastructure.repository;

import de.hochschule.augsburg.registration.infrastructure.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, UUID> {

    Optional<RegistrationEntity> findByStudent(String student);

    @Query("SELECT r FROM hsa_registration r WHERE r.registrationWindowId = :registrationWindowId AND :subjectId IN (SELECT sl.id from r.subjectSelection sl)")
    List<RegistrationEntity> findByRegistrationWindowAndSubject(@Param("registrationWindowId") UUID registrationWindowId, @Param("subjectId") UUID subjectId);

}
