package br.com.jogatinastore.iam.role.infrastructure.persistence;

import br.com.jogatinastore.iam.role.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByTitle(String title);

    List<Role> findAllByIdIn(List<UUID> ids);
}
