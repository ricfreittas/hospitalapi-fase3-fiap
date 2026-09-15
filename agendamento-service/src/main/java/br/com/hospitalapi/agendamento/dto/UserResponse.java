package br.com.hospitalapi.agendamento.dto;

import br.com.hospitalapi.agendamento.model.Role;

public record UserResponse(

        Long id,
        String name,
        String email,
        Role role
) {
}
