package br.com.hospitalapi.agendamento.repository;

import br.com.hospitalapi.agendamento.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
