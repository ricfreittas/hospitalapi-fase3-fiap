package br.com.hospitalapi.agendamento.dto;

import jakarta.validation.constraints.NotNull;

public record PatientRequest(
        @NotNull
        Long userId
) {}
