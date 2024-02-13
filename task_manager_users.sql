INSERT INTO usuarios (user_id,username, nombres, apellidos, email, pw) 
VALUES ('1','dacm', 'Daniel', 'Contreras', 'daniel@gmail.com', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q'),
('2','laura', 'Laura', 'González', 'laura@gmail.com', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q'),
('3','juanito', 'Juan', 'Pérez', 'juanito@gmail.com', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q'),
('4','luisfer', 'luis', 'Pérez', 'luisfer@gmail.com', '{bcrypt}$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q');


INSERT INTO roles (user_id, rol) 
VALUES ('1', 'ROLE_ADMIN'),
('2',  'ROLE_USER'),
('3', 'ROLE_USER'),
('4', 'ROLE_USER');
