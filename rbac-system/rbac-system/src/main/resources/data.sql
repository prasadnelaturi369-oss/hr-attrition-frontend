-- data.sql for H2 Database with table existence checks

-- Only insert if tables exist
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insert Permissions (only if empty)
MERGE INTO permissions (name, description) KEY(name) VALUES 
('USER_VIEW', 'View user information'),
('USER_CREATE', 'Create new users'),
('USER_UPDATE', 'Update user information'),
('USER_DELETE', 'Delete users'),
('ROLE_VIEW', 'View roles'),
('ROLE_CREATE', 'Create new roles'),
('ROLE_UPDATE', 'Update roles'),
('ROLE_DELETE', 'Delete roles'),
('ROLE_ASSIGN', 'Assign roles to users'),
('ROLE_REMOVE', 'Remove roles from users'),
('PERMISSION_VIEW', 'View permissions'),
('PERMISSION_ASSIGN', 'Assign permissions to roles');

-- Insert Roles (only if empty)
MERGE INTO roles (name, description) KEY(name) VALUES 
('ROLE_SUPER_ADMIN', 'Super Administrator with full system access'),
('ROLE_ADMIN', 'Administrator with elevated access'),
('ROLE_MANAGER', 'Manager with user management capabilities'),
('ROLE_USER', 'Regular user with basic access');

-- Assign all permissions to Super Admin
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id 
FROM roles r, permissions p 
WHERE r.name = 'ROLE_SUPER_ADMIN'
AND NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

-- Assign permissions to Admin
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id 
FROM roles r, permissions p 
WHERE r.name = 'ROLE_ADMIN' 
AND p.name IN ('USER_VIEW', 'USER_CREATE', 'USER_UPDATE', 'USER_DELETE', 
               'ROLE_VIEW', 'ROLE_ASSIGN', 'ROLE_REMOVE', 'PERMISSION_VIEW')
AND NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

-- Assign permissions to Manager
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id 
FROM roles r, permissions p 
WHERE r.name = 'ROLE_MANAGER' 
AND p.name IN ('USER_VIEW', 'USER_CREATE', 'USER_UPDATE', 'ROLE_VIEW')
AND NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

-- Assign permissions to User
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id 
FROM roles r, permissions p 
WHERE r.name = 'ROLE_USER' 
AND p.name IN ('USER_VIEW')
AND NOT EXISTS (SELECT 1 FROM role_permissions rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

-- Create default super admin user (password: Admin@123) - only if not exists
MERGE INTO users (username, email, password, first_name, last_name, enabled, created_at, updated_at) KEY(username) 
VALUES ('admin', 'admin@rbac.com', '$2a$10$rQhJ5z.zCJQJiJcLQ8YVHOXU9VCnEXQGgPIMIcKJNUPQzE8JQXZGy', 'System', 'Administrator', true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Assign super admin role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_SUPER_ADMIN'
AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);