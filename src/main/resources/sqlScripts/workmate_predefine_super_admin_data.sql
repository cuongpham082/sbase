INSERT INTO users (email, username, password, stamp) VALUES
('admin@testingemail.com', 'admin', '$2a$10$8BzgQDkHCUihfBOkkSg9ZelpONaynV06DgKP/audHEw4z3mX4L1bK', 1);

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1);