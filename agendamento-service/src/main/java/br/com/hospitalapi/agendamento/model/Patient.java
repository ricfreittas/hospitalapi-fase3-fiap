package br.com.hospitalapi.agendamento.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Table(name = "patients")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name  = "user_id",  nullable = false,  unique = true)
    private User user;
}
