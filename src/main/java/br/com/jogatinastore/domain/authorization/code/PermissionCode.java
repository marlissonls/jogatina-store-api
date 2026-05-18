package br.com.jogatinastore.domain.authorization.code;

public enum PermissionCode {

    // Roles
    ROLE_ADMIN("ROLE_ADMIN", "Full access to all system resources and administrative settings."),
    ROLE_MANAGER("ROLE_MANAGER", "Management access to oversee users, reports, and operational workflows."),
    ROLE_CUSTOMER("ROLE_CUSTOMER", "Standard customer access to view products and manage personal profile."),

    // Permissions
    USER_WRITE("USER_WRITE", "Can manage users");

    private final String key;
    private final String description;

    PermissionCode(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String key() {
        return key;
    }

    public String description() {
        return description;
    }
}
