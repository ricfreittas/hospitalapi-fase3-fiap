package br.com.hospitalapi.agendamento.service;

import br.com.hospitalapi.agendamento.dto.PatientRequest;
import br.com.hospitalapi.agendamento.dto.PatientResponse;
import br.com.hospitalapi.agendamento.exception.ResourceNotFoundException;
import br.com.hospitalapi.agendamento.model.Patient;
import br.com.hospitalapi.agendamento.model.Role;
import br.com.hospitalapi.agendamento.model.User;
import br.com.hospitalapi.agendamento.repository.PatientRepository;
import br.com.hospitalapi.agendamento.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public PatientResponse create(PatientRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (user.getRole() != Role.PATIENT) {
            throw new ResourceNotFoundException("Não é permitido criar o paciente.");
        }

        Patient patient = Patient.builder()
                .user(user)
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return new PatientResponse(
                savedPatient.getId(),
                savedPatient.getUser().getId(),
                savedPatient.getUser().getName(),
                savedPatient.getUser().getEmail()
        );
    }
}
