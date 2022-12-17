package de.hochschule.augsburg.registration.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data to create a new registration")
public class NewRegistrationTO {

    @NotNull
    @NotBlank
    private String student;

    @NotNull
    @Size(min = 1)
    private List<NewSubjectSelectionTO> subjectSelection;

}
