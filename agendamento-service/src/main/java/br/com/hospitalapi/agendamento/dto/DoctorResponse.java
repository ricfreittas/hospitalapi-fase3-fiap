package br.com.hospitalapi.agendamento.dto;

public record DoctorResponse(
        Long id,
        Long userId,
        String name,
        String email
) {
}
