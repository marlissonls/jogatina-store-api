package br.com.jogatinastore.domain.iam.authorization.repository;

import br.com.jogatinastore.domain.iam.authorization.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByTitle(String title);
}
