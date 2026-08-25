package br.com.jogatinastore.domain.iam.user.entity;

import br.com.jogatinastore.domain.iam.role.entity.Role;
import br.com.jogatinastore.domain.iam.user.exception.UserErrors;
import br.com.jogatinastore.infra.exception.CannotRemoveLastRoleException;
import br.com.jogatinastore.infra.exception.RoleNotAssignedException;
import jakarta.persistence.*;

import jakarta.validation.Valid;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP, enabled = false WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired = true;

    private Boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Valid
    @ElementCollection
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
    )
    private Set<UserRole> userRoles = new HashSet<>();

    public User() {}

    public void assignRole(Role role) {
        this.userRoles.add(new UserRole(role));
    }

    public void assignRoles(Collection<Role> roles) {
        roles.forEach(this::assignRole);
    }

    public void removeRole(UUID roleId) {
        boolean isPresent = userRoles.stream()
                .anyMatch(userRole -> userRole.getRole().getId().equals(roleId));

        if (!isPresent) {
            throw new RoleNotAssignedException(
                    UserErrors.Target.USER_ROLE,
                    UserErrors.Code.USER_ROLE_NOT_ASSIGNED
            );
        }

        if (userRoles.size() == 1) {
            throw new CannotRemoveLastRoleException(
                    UserErrors.Target.USER_ROLE,
                    UserErrors.Code.USER_ROLE_CANNOT_REMOVE_LAST
            );
        }

        userRoles.removeIf(
                userRole -> userRole.getRole().getId().equals(roleId)
        );
    }

    public List<String> getRoles() {
        return this.userRoles.stream()
            .map(up -> up.getRole().getTitle())
            .collect(Collectors.toList());
    }
    
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles.stream()
            .map(UserRole::getRole)
            .collect(Collectors.toSet());
    }

    public String getPassword() {
        return this.passwordHash;
    }

    public String getUsername() {
        return this.email;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}