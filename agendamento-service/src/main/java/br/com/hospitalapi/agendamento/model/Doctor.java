package br.com.hospitalapi.agendamento.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "doctors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne(optional = false)
        @JoinColumn(name = "user_id",  nullable = false,  unique = true)
        private User user;


}
