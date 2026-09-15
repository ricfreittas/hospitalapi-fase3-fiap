package br.com.hospitalapi.agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(

        @NotNull
        Long patientId,

        @NotNull
        Long doctorId,

        @NotNull
        @Future
        LocalDateTime appointmentDateTime,

        String notes
) {

}
