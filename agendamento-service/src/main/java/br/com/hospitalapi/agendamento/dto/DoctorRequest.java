package br.com.hospitalapi.agendamento.dto;

import jakarta.validation.constraints.NotNull;

public record DoctorRequest(
        @NotNull
        Long userId
) {
}
