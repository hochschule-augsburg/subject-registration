package de.hochschule.augsburg.registration.infrastructure.entity;

import de.hochschule.augsburg.registration.domain.model.RegistrationStatus;
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
@Entity(name = "hsa_registration")
@Table(name = "hsa_registration")
public class RegistrationEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "student", nullable = false)
    private String student;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_", nullable = false)
    private RegistrationStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "registration_id")
    private List<SubjectSelectionEntity> subjectSelection;

    @Column(name = "registration_window_id", columnDefinition = "BINARY(16)")
    private UUID registrationWindowId;
}
