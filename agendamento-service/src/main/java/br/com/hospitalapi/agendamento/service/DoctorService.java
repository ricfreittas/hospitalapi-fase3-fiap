package br.com.hospitalapi.agendamento.service;

import br.com.hospitalapi.agendamento.dto.DoctorRequest;
import br.com.hospitalapi.agendamento.dto.DoctorResponse;
import br.com.hospitalapi.agendamento.exception.ResourceNotFoundException;
import br.com.hospitalapi.agendamento.model.Doctor;
import br.com.hospitalapi.agendamento.model.Role;
import br.com.hospitalapi.agendamento.model.User;
import br.com.hospitalapi.agendamento.repository.DoctorRepository;
import br.com.hospitalapi.agendamento.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            UserRepository userRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    public DoctorResponse create(DoctorRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.DOCTOR) {
            throw new ResourceNotFoundException("User is not a doctor");
        }

        Doctor doctor = Doctor.builder()
                .user(user)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return new DoctorResponse(
                savedDoctor.getId(),
                savedDoctor.getUser().getId(),
                savedDoctor.getUser().getName(),
                savedDoctor.getUser().getEmail()
        );
    }
}