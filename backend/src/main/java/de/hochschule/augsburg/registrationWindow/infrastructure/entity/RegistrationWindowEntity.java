package de.hochschule.augsburg.registrationWindow.infrastructure.entity;

import de.hochschule.augsburg.registration.infrastructure.entity.RegistrationEntity;
import de.hochschule.augsburg.registration.infrastructure.entity.SubjectSelectionEntity;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "hsa_registration_window")
public class RegistrationWindowEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    public UUID id;

    @Column(name = "semester", nullable = false)
    private String semester;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationWindowStatus status;



}
