package br.com.jogatinastore.domain.iam.role.entity;

import br.com.jogatinastore.domain.iam.role.dto.RoleCreateDTO;
import br.com.jogatinastore.domain.iam.role.dto.RoleUpdateDTO;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column
    private String description;

    protected Role() {}

    public Role(
        String title,
        String description
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
    }

    public static Role createFrom(RoleCreateDTO dto) {
        return new Role(
                dto.title(),
                dto.description()
        );
    }

    public void applyUpdate(RoleUpdateDTO dto) {
        this.title = dto.title();
        this.description = dto.description();
    }

    @Override
    public String getAuthority() {
        return this.description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
