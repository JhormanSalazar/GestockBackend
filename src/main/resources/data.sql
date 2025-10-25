-- =====================================================
-- Gestock - Initialization Script
-- =====================================================
-- Creates:
-- 1. SYSTEM business for admin user
-- 2. Three roles: ADMIN, BUSINESS_OWNER, COLLABORATOR
-- 3. Admin user (email: admin@gestock.com, password: admin123)
-- =====================================================
-- NOTE: This script runs automatically on application startup
-- Uses INSERT ... ON CONFLICT to avoid duplicates
-- =====================================================

-- Insert SYSTEM business (for admin user)
INSERT INTO businesses (id, name)
VALUES (1, 'SYSTEM')
ON CONFLICT (id) DO NOTHING;

-- Insert roles
INSERT INTO roles (id, name)
VALUES (1, 'ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (id, name)
VALUES (2, 'BUSINESS_OWNER')
ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (id, name)
VALUES (3, 'COLLABORATOR')
ON CONFLICT (id) DO NOTHING;

-- Insert admin user
-- Email: admin@gestock.com
-- Password: admin123
-- BCrypt hash generated with strength 10
INSERT INTO users (id, email, password, business_id, role_id)
VALUES (
    1,
    'admin@gestock.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    1,
    1
)
ON CONFLICT (id) DO NOTHING;

-- Reset sequences to avoid ID conflicts with future inserts
-- This ensures new businesses start at ID 2, new roles at ID 4, new users at ID 2
SELECT setval('businesses_id_seq', (SELECT MAX(id) FROM businesses), true);
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles), true);
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users), true);
