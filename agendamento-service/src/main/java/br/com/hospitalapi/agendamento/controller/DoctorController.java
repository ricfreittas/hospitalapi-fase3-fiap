package br.com.hospitalapi.agendamento.controller;

import br.com.hospitalapi.agendamento.dto.DoctorRequest;
import br.com.hospitalapi.agendamento.dto.DoctorResponse;
import br.com.hospitalapi.agendamento.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(
            @Valid @RequestBody DoctorRequest request
    ) {
        DoctorResponse response = doctorService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}