INSERT INTO user_account (login, name, password, my_role) VALUES
('admin', 'Admin', '$2a$10$3KJqNMX4tuZtTKztck8jhevhjjcBtGnXrFq14XjNnQfwt/dO1EK06', 'Admin')
ON CONFLICT DO NOTHING;