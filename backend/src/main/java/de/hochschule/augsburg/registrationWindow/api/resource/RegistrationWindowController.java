package de.hochschule.augsburg.registrationWindow.api.resource;

import de.hochschule.augsburg.registrationWindow.api.mapper.RegistrationWindowApiMapper;
import de.hochschule.augsburg.registrationWindow.api.transport.NewRegistrationWindowTO;
import de.hochschule.augsburg.registrationWindow.api.transport.RegistrationWindowTO;
import de.hochschule.augsburg.registrationWindow.api.transport.RegistrationWindowUpdateTO;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindow;
import de.hochschule.augsburg.registrationWindow.domain.model.RegistrationWindowUpdate;
import de.hochschule.augsburg.registrationWindow.domain.service.RegistrationWindowService;
import de.hochschule.augsburg.security.SecurityService;
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
@RestController
@RequiredArgsConstructor
@Tag(name = "RegistrationWindow Controller")
@RequestMapping("/api/registration_window")
@CrossOrigin
public class RegistrationWindowController {
    private final SecurityService securityService;
    private final RegistrationWindowService registrationWindowService;
    private final RegistrationWindowApiMapper registrationWindowApiMapper;
    private final UserContext userContext;


    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "Get list of all registration windows")
    public ResponseEntity<List<RegistrationWindowTO>> getAllRegistrationWindows() {
        log.debug("Received request to get all registrationPeriods");
        final List<RegistrationWindow> allRegistrationWindows = this.registrationWindowService.getAllRegistrationWindows();
        return ResponseEntity.ok().body(this.registrationWindowApiMapper.map(allRegistrationWindows));
    }


    @GetMapping("/active")
    @Transactional(readOnly = true)
    @Operation(summary = "Get an active registration window")
    public ResponseEntity<RegistrationWindowTO> getActiveRegistrtionWindow() {
        log.debug("Received request to get active registration window");
        final RegistrationWindow activeRegistrationWindow = this.registrationWindowService.getActiveRegistrationWindow();
        return ResponseEntity.ok().body(this.registrationWindowApiMapper.map(activeRegistrationWindow));
    }

    @Transactional
    @PutMapping()
    @Operation(summary = "Update an existing registration window")
    public ResponseEntity<RegistrationWindowTO> updateRegistrationWindow(
            @RequestBody @Valid final RegistrationWindowUpdateTO updateTO
    ) {
        log.debug("Received request to update the registration window with the id '{}'", updateTO.getId());
        final RegistrationWindow registrationWindow = this.registrationWindowService.updateRegistrationWindow(this.registrationWindowApiMapper.map(updateTO), this.userContext.getLoggedInUser());
        return ResponseEntity.ok(this.registrationWindowApiMapper.map(registrationWindow));
    }

    @Transactional
    @DeleteMapping("/{registrationWindowId}")
    @Operation(summary = "Delete an existing registration window")
    public ResponseEntity<Void> deleteRegistrationWindow(@PathVariable("registrationWindowId") final UUID registrationPeriodId) {
        log.debug("Received request to delete the registration period with the id '{}'", registrationPeriodId);
        this.registrationWindowService.deleteRegistrationWindow(registrationPeriodId, this.userContext.getLoggedInUser());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping
    //@Secured({"ROLE_hsa-ROLE_professoren", "ROLE_lehrbeauftr"})
    @Operation(summary = "Create a new registration window")
    public ResponseEntity<RegistrationWindowTO> createNewRegistrationWindow(@RequestBody @Valid final NewRegistrationWindowTO newRegistrationWindowTO) {
        log.debug("Received request to create a new registration period: {}", newRegistrationWindowTO);
        if (!this.securityService.isProfessor()) {// Damit werden auch user Rechte ueberprueft
            return ResponseEntity.status(403).build();
        }
        final RegistrationWindow registrationWindow = this.registrationWindowService.createRegistrationWindow(this.registrationWindowApiMapper.map(newRegistrationWindowTO), this.userContext.getLoggedInUser());
        final RegistrationWindowUpdate registrationWindowUpdate = new RegistrationWindowUpdate(registrationWindow.getId(), registrationWindow.getStartDate(), registrationWindow.getEndDate());
        registrationWindow.update(registrationWindowUpdate);
        return ResponseEntity.ok(this.registrationWindowApiMapper.map(registrationWindow));
    }

}
