package br.com.hospitalapi.agendamento.repository;

import br.com.hospitalapi.agendamento.model.Appointment;
import br.com.hospitalapi.agendamento.model.AppointmentStatus;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientUserEmail(String email);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatientIdAndAppointmentDateTimeAfter(
            Long patientId,
            LocalDateTime dateTime);
}
