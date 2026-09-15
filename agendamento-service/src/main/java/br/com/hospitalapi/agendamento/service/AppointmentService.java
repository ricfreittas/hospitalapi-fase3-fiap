package br.com.hospitalapi.agendamento.service;

import br.com.hospitalapi.agendamento.dto.AppointmentRequest;
import br.com.hospitalapi.agendamento.dto.AppointmentResponse;
import br.com.hospitalapi.agendamento.dto.AppointmentUpdateRequest;
import br.com.hospitalapi.agendamento.exception.AccessDeniedBusinessException;
import br.com.hospitalapi.agendamento.exception.ResourceNotFoundException;
import br.com.hospitalapi.agendamento.messaging.AppointmentEvent;
import br.com.hospitalapi.agendamento.messaging.AppointmentEventProducer;
import br.com.hospitalapi.agendamento.model.Appointment;
import br.com.hospitalapi.agendamento.model.AppointmentStatus;
import br.com.hospitalapi.agendamento.model.Doctor;
import br.com.hospitalapi.agendamento.model.Patient;
import br.com.hospitalapi.agendamento.repository.AppointmentRepository;
import br.com.hospitalapi.agendamento.repository.DoctorRepository;
import br.com.hospitalapi.agendamento.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentEventProducer appointmentEventProducer;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentEventProducer appointmentEventProducer
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentEventProducer = appointmentEventProducer;
    }

    public AppointmentResponse create(AppointmentRequest request) {

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(request.appointmentDateTime())
                .status(AppointmentStatus.SCHEDULED)
                .notes(request.notes())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentEvent event = new AppointmentEvent(
                "APPOINTMENT_CREATED",
                savedAppointment.getId(),
                savedAppointment.getPatient().getId(),
                savedAppointment.getDoctor().getId(),
                savedAppointment.getAppointmentDateTime()
        );

        appointmentEventProducer.publish(event);

        return toResponse(savedAppointment);

    }

    public AppointmentResponse findById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        return toResponse(appointment);
    }

    private AppointmentResponse toResponse(Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }

    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AppointmentResponse update(
            Long id,
            AppointmentUpdateRequest request
    ) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(request.appointmentDateTime());
        appointment.setStatus(request.status());
        appointment.setNotes(request.notes());

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        AppointmentEvent event = new AppointmentEvent(
                "APPOINTMENT_UPDATED",
                updatedAppointment.getId(),
                updatedAppointment.getPatient().getId(),
                updatedAppointment.getDoctor().getId(),
                updatedAppointment.getAppointmentDateTime()
        );

        appointmentEventProducer.publish(event);

        return toResponse(updatedAppointment);
    }

    public List<AppointmentResponse> findByPatientEmail(String email) {
        return appointmentRepository.findByPatientUserEmail(email).
                stream().
                map(this::toResponse)
                .toList();
    }

    public AppointmentResponse findByIdForUser(
            Long id,
            String email,
            boolean isPatient
    ) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agendamento não encontrado"));

        if (isPatient &&
                !appointment.getPatient().getUser().getEmail().equals(email)) {

            throw new AccessDeniedBusinessException("Você não pode acessar este agendamento.");
        }

        return toResponse(appointment);
    }

    public List<AppointmentResponse> findByPatientId(Long patientId) {

        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> findFutureByPatientId(Long patientId) {

        return appointmentRepository
                .findByPatientIdAndAppointmentDateTimeAfter(
                        patientId,
                        LocalDateTime.now()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<AppointmentResponse> findByPatientIdForUser(
            Long patientId,
            String email,
            boolean isPatient
    ) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Paciente não encontrado")
                );

        if (isPatient &&
                !patient.getUser().getEmail().equals(email)) {

            throw new AccessDeniedBusinessException(
                    "Você não pode acessar as consultas deste paciente."
            );
        }

        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AppointmentResponse> findFutureByPatientIdForUser(
            Long patientId,
            String email,
            boolean isPatient
    ) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Paciente não encontrado")
                );

        if (isPatient &&
                !patient.getUser().getEmail().equals(email)) {

            throw new AccessDeniedBusinessException(
                    "Você não pode acessar as consultas deste paciente."
            );
        }

        return appointmentRepository
                .findByPatientIdAndAppointmentDateTimeAfter(
                        patientId,
                        LocalDateTime.now()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }
}