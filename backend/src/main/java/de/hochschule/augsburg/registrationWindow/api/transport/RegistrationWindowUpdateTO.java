package de.hochschule.augsburg.registrationWindow.api.transport;

import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data to update a registration window")
public class RegistrationWindowUpdateTO {
    @NotNull
    @NotBlank
    private UUID id;

    @NotNull
    @NotBlank
    private String startDate;

    @NotNull
    @NotBlank
    private String endDate;

}
