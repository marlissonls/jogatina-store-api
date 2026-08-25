package br.com.jogatinastore.domain.iam.user.entity;

import br.com.jogatinastore.domain.iam.role.entity.Role;
import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class UserRole {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    protected UserRole() {}

    public UserRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole other)) return false;
        return Objects.equals(role, other.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}