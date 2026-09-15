package br.com.hospitalapi.agendamento.controller;

import br.com.hospitalapi.agendamento.dto.AppointmentRequest;
import br.com.hospitalapi.agendamento.dto.AppointmentResponse;
import br.com.hospitalapi.agendamento.dto.AppointmentUpdateRequest;
import br.com.hospitalapi.agendamento.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {

        AppointmentResponse response = appointmentService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(
            @PathVariable Long id,
            Authentication authentication

    ) {

        boolean isPatient = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT"));

        AppointmentResponse response = appointmentService.findByIdForUser(
                id,
                authentication.getName()
                ,isPatient
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> findAll(
            Authentication authentication
    ) {

       boolean isPatient = authentication.getAuthorities()
               .stream()
               .anyMatch(authority ->
                       authority.getAuthority().equals("ROLE_PATIENT")
               );

       List<AppointmentResponse> appointments;

       if (isPatient) {
           appointments = appointmentService.findByPatientEmail(authentication.getName());
       } else {
           appointments = appointmentService.findAll();
       }
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> update(
                    @PathVariable Long id,
                    @Valid @RequestBody AppointmentUpdateRequest request
    ) {
        AppointmentResponse response = appointmentService.update(id, request);

        return ResponseEntity.ok(response);
    }
}
