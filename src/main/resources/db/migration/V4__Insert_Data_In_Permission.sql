INSERT INTO permission (id, description) VALUES
     (UUID(), 'ADMIN'),
     (UUID(), 'MANAGER'),
     (UUID(), 'COMMON_USER');

-- INSERT INTO permission (id, description)
-- SELECT UUID(), 'ADMIN'
--     WHERE NOT EXISTS (SELECT 1 FROM permission WHERE description = 'ADMIN');
--
-- INSERT INTO permission (id, description)
-- SELECT UUID(), 'MANAGER'
--     WHERE NOT EXISTS (SELECT 1 FROM permission WHERE description = 'MANAGER');
--
-- INSERT INTO permission (id, description)
-- SELECT UUID(), 'COMMON_USER'
--     WHERE NOT EXISTS (SELECT 1 FROM permission WHERE description = 'COMMON_USER');