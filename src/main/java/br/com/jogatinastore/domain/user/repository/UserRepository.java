package br.com.jogatinastore.domain.user.repository;

import br.com.jogatinastore.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoles up " +
            "LEFT JOIN FETCH up.role " +
            "WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") UUID id);

    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.userRoles up
        LEFT JOIN FETCH up.role
        WHERE u.id IN :ids
    """)
    List<User> findAllWithRolesByIdIn(List<UUID> ids);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.userRoles up " +
            "JOIN FETCH up.role " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = false " +
            "WHERE u.id =:id " +
            "AND enabled = true")
    void deactivate(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = true " +
            "WHERE u.id =:id " +
            "AND enabled = false")
    void activate(@Param("id") UUID id);
}
