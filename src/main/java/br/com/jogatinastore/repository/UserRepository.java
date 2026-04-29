package br.com.jogatinastore.repository;

import br.com.jogatinastore.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT COUNT(*) FROM users WHERE email = :email", nativeQuery = true)
    Long existsAnyByEmailIncludingDeleted(@Param("email") String email);

    @Query(value = "SELECT COUNT(*) FROM users WHERE cpf = :cpf", nativeQuery = true)
    Long existsAnyByCpfIncludingDeleted(@Param("cpf") String cpf);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userPermissions up " +
            "LEFT JOIN FETCH up.permission " +
            "WHERE u.id = :id")
    Optional<User> findByIdWithPermissions(@Param("id") UUID id);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.userPermissions up " +
            "LEFT JOIN FETCH up.permission")
    List<User> findAllWithPermissions();

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.userPermissions up " +
            "JOIN FETCH up.permission " +
            "WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
