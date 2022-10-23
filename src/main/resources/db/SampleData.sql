INSERT INTO users (id, account_expired, account_locked, credentials_expired, email, enabled, password, created_at, updated_at)
SELECT '906a1941-b573-468a-ba6a-d9ab70999a55', false, false, false, 'user@example.com', true, '$2a$10$gkktnlsTPO3QpF9rw2y5De4Z3jbpdedMAhw1oEjVg9wGHLxGxgrwy', CURRENT_DATE, CURRENT_DATE
WHERE NOT EXISTS(SELECT * FROM users WHERE id = '906a1941-b573-468a-ba6a-d9ab70999a55');

INSERT INTO users (id, account_expired, account_locked, credentials_expired, email, enabled, password, created_at, updated_at)
SELECT 'afe7eaee-09cc-4e50-a166-bf2c8c48017d', false, false, false, 'admin@example.com', true, '$2a$10$gkktnlsTPO3QpF9rw2y5De4Z3jbpdedMAhw1oEjVg9wGHLxGxgrwy', CURRENT_DATE, CURRENT_DATE
WHERE NOT EXISTS(SELECT * FROM users WHERE id = 'afe7eaee-09cc-4e50-a166-bf2c8c48017d');

INSERT INTO users_roles (users_id, user_roles)
SELECT '906a1941-b573-468a-ba6a-d9ab70999a55', 'ROLE_USER'
WHERE NOT EXISTS(SELECT * FROM users_roles WHERE users_id = '906a1941-b573-468a-ba6a-d9ab70999a55');

INSERT INTO users_roles (users_id, user_roles)
SELECT 'afe7eaee-09cc-4e50-a166-bf2c8c48017d', 'ROLE_ADMIN'
WHERE NOT EXISTS(SELECT * FROM users_roles WHERE users_id = 'afe7eaee-09cc-4e50-a166-bf2c8c48017d');
