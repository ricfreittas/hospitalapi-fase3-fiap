package br.com.hospitalapi.agendamento.dto;

import br.com.hospitalapi.agendamento.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequest(

        @NotNull
        String name,

        @NotNull
        @Email
        String email,

        @NotNull
        String password,

        @NotNull
        Role role
) {
}
