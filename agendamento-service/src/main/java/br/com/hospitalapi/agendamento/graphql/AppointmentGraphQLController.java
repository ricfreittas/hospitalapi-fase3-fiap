package br.com.hospitalapi.agendamento.graphql;

import br.com.hospitalapi.agendamento.dto.AppointmentResponse;
import br.com.hospitalapi.agendamento.service.AppointmentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppointmentGraphQLController {

    private final AppointmentService appointmentService;

    public AppointmentGraphQLController(
            AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @QueryMapping
    public List<AppointmentResponse> appointments(
            Authentication authentication
    ) {

        boolean isPatient = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT")
                );

        if (isPatient) {
            return appointmentService
                    .findByPatientEmail(authentication.getName());
        }

        return appointmentService.findAll();
    }

    @QueryMapping
    public List<AppointmentResponse> appointmentsByPatient(
            @Argument Long patientId,
            Authentication authentication
    ) {

        boolean isPatient = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT")
                );

        return appointmentService.findByPatientIdForUser(
                patientId,
                authentication.getName(),
                isPatient
        );
    }

    @QueryMapping
    public List<AppointmentResponse> futureAppointmentsByPatient(
            @Argument Long patientId,
            Authentication authentication
    ) {

        boolean isPatient = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_PATIENT")
                );

        return appointmentService.findFutureByPatientIdForUser(
                patientId,
                authentication.getName(),
                isPatient
        );
    }
}