package br.com.hospitalapi.agendamento.controller;

import br.com.hospitalapi.agendamento.dto.PatientRequest;
import br.com.hospitalapi.agendamento.dto.PatientResponse;
import br.com.hospitalapi.agendamento.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> create(
            @Valid @RequestBody PatientRequest request
    ) {
        PatientResponse response = patientService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}