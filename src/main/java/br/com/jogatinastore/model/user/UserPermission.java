package br.com.jogatinastore.model.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_permission")
public class UserPermission {

    @EmbeddedId
    private UserPermissionId id;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY avoids infinite recall with User that is EAGER.
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "id_permission")
    private Permission permission;

    public UserPermission() {}

    public UserPermission(User user, Permission permission) {
        this.id = new UserPermissionId(user.getId(), permission.getId());
        this.user = user;
        this.permission = permission;
    }

    public UserPermissionId getId() {
        return id;
    }

    public void setId(UserPermissionId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPermission that = (UserPermission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}