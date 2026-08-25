package br.com.jogatinastore.domain.iam.role.repository;

import br.com.jogatinastore.domain.iam.role.code.RoleCode;
import br.com.jogatinastore.domain.iam.role.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByTitle(String title);

    List<Role> findAllByIdIn(List<UUID> ids);
}
