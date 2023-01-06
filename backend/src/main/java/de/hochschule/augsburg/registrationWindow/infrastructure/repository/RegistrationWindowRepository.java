package de.hochschule.augsburg.registrationWindow.infrastructure.repository;

import de.hochschule.augsburg.registrationWindow.infrastructure.entity.RegistrationWindowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface RegistrationWindowRepository extends JpaRepository<RegistrationWindowEntity, UUID> {
    @Query("SELECT r FROM hsa_registration_window r WHERE r.status= 'active' ")
    RegistrationWindowEntity findOpenRegistrationWindow();
}
