package br.com.hospitalapi.agendamento.dto;

import br.com.hospitalapi.agendamento.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentUpdateRequest(

        @NotNull
        Long patientId,

        @NotNull
        Long doctorId,

        @NotNull
        LocalDateTime appointmentDateTime,

        @NotNull
        AppointmentStatus status,

        String notes
) {
}
