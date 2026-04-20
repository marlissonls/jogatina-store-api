CREATE TABLE IF NOT EXISTS user_permission (
    id_user CHAR(36) NOT NULL,
    id_permission CHAR(36) NOT NULL,
    PRIMARY KEY(id_user, id_permission),

    KEY idx_user_permission_id_permission (id_permission), -- index for better search permissions performance

    CONSTRAINT fk_user_permission_user
    FOREIGN KEY (id_user) REFERENCES users (id)
    ON DELETE CASCADE,

    CONSTRAINT fk_user_permission_permission
    FOREIGN KEY (id_permission) REFERENCES permission (id)
    ON DELETE CASCADE
) ENGINE=InnoDB;