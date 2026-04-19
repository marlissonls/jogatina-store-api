package br.com.jogatinastore.repository;

import br.com.jogatinastore.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);
}
