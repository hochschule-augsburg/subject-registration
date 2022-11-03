package de.hochschule.augsburg.registrationWindow.infrastructure.entity;

import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "hsa_registration_window")
public class RegistrationWindowEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, updatable = false, length = 36)
    public String id;

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
