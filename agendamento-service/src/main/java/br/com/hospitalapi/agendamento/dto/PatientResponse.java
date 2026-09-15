package br.com.hospitalapi.agendamento.dto;

public record PatientResponse(

        Long id,
        Long userId,
        String name,
        String email
) {
}