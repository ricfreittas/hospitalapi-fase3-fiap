package br.com.hospitalapi.notificacao.messaging;

import java.time.LocalDateTime;

public record AppointmentEvent(
        String eventType,
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime
) {
}
