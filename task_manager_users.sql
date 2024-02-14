INSERT INTO usuarios (username, nombres, apellidos, email, pw) 
VALUES ('dacm', 'Daniel', 'Contreras', 'daniel@gmail.com', '$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q'),
('dacm1', 'Daniel', 'González', 'laura@gmail.com', '$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q'),
('juanito', 'Juan', 'Pérez', 'juanito@gmail.com', '$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q'),
('luisfer', 'luis', 'Pérez', 'luisfer@gmail.com', '$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q');


INSERT INTO roles (user_id, rol) 
VALUES ('1', 'ROLE_ADMIN'),
('2',  'ROLE_USER'),
('3', 'ROLE_USER'),
('4', 'ROLE_USER');
