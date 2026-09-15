package br.com.hospitalapi.agendamento.messaging;

import java.time.LocalDateTime;

public record AppointmentEvent(
        String eventType,
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime
) {
}
