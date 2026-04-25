package br.com.jogatinastore.repository;

import br.com.jogatinastore.model.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByTitle(String title);
}
