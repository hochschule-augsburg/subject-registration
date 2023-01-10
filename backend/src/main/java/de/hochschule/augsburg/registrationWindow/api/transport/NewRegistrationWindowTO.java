package de.hochschule.augsburg.registrationWindow.api.transport;

import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information to a new registration window")
public class NewRegistrationWindowTO {
    @NotNull
    @NotBlank
    private String semester;

    @Basic
    private LocalDateTime startDate;

    @Basic
    private LocalDateTime endDate;

    @NotNull
    private RegistrationWindowStatus status;

}
