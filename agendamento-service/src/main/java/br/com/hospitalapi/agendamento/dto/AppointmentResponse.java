package br.com.hospitalapi.agendamento.dto;

import br.com.hospitalapi.agendamento.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime,
        AppointmentStatus status,
        String notes
) {
}
