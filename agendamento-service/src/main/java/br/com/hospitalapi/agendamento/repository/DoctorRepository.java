package br.com.hospitalapi.agendamento.repository;

import br.com.hospitalapi.agendamento.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
