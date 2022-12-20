package de.hochschule.augsburg.registrationWindow.infrastructure.repository;

import de.hochschule.augsburg.registrationWindow.infrastructure.entity.RegistrationWindowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RegistrationWindowRepository extends JpaRepository<RegistrationWindowEntity, UUID> {

}
