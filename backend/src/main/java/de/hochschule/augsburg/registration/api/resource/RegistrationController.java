package de.hochschule.augsburg.registration.api.resource;

import de.hochschule.augsburg.registration.api.mapper.RegistrationApiMapper;
import de.hochschule.augsburg.registration.api.transport.NewRegistrationTO;
import de.hochschule.augsburg.registration.api.transport.RegistrationTO;
import de.hochschule.augsburg.registration.api.transport.RegistrationUpdateTO;
import de.hochschule.augsburg.registration.domain.model.Registration;
import de.hochschule.augsburg.registration.domain.service.RegistrationService;
import de.hochschule.augsburg.security.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Registration Controller")
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final RegistrationApiMapper registrationApiMapper;
    private final UserContext userContext;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Get list of all Registrations")
    public ResponseEntity<List<RegistrationTO>> getAllRegistrations() {
        log.debug("Received request to get all registrations");
        final List<Registration> allProjects = this.registrationService.getAllRegistrations(this.userContext.getLoggedInUser());
        return ResponseEntity.ok().body(this.registrationApiMapper.map(allProjects));
    }

    @GetMapping("/{uid}")
    @Transactional(readOnly = true)
    @Operation(summary = "Get registration by student")
    public ResponseEntity<RegistrationTO> getRegistration(@PathVariable final String uid) {
        log.debug("Received request to get all registrations");
        final Registration registration = this.registrationService.getRegistrationByStudent(uid);
        return ResponseEntity.ok().body(this.registrationApiMapper.map(registration));
    }

    @Transactional
    @PostMapping
    @Operation(summary = "Create a new registration")
    public ResponseEntity<RegistrationTO> createNewRegistration(@RequestBody @Valid final NewRegistrationTO newRegistrationTO) {
        log.debug("Received request to create a new registration: {}", newRegistrationTO);
        final Registration registration = this.registrationService.createRegistration(this.registrationApiMapper.map(newRegistrationTO), this.userContext.getLoggedInUser(), this.userContext.getLoggedInUserMail());
        return ResponseEntity.ok(this.registrationApiMapper.map(registration));
    }

    @Transactional
    @PutMapping()
    @Operation(summary = "Update an existing registration")
    public ResponseEntity<RegistrationTO> updateRegistration(
            @RequestBody @Valid final RegistrationUpdateTO updateTO
    ) {
        log.debug("Received request to update the registration with the id '{}'", updateTO.getId());
        final Registration registration = this.registrationService.updateRegistration(this.registrationApiMapper.map(updateTO), this.userContext.getLoggedInUser());
        return ResponseEntity.ok(this.registrationApiMapper.map(registration));
    }

    @Transactional
    @DeleteMapping("/{registrationId}")
    @Operation(summary = "Delete an existing registration")
    public ResponseEntity<Void> deleteRegistration(@PathVariable("registrationId") final UUID registrationId) {
        log.debug("Received request to delete the registration with the id '{}'", registrationId);
        this.registrationService.deleteRegistration(registrationId, this.userContext.getLoggedInUser());
        return ResponseEntity.ok().build();
    }
}
