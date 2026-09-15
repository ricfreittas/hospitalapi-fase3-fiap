package br.com.hospitalapi.agendamento.repository;

import br.com.hospitalapi.agendamento.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
}
