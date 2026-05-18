package br.com.jogatinastore.domain.authorization.repository;

import br.com.jogatinastore.domain.authorization.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByTitle(String title);
}
