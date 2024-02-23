-- Insertamos algunos usuarios
INSERT INTO users (username, firstname, lastname, email, pw, role) VALUES 
('user1', 'John', 'Doe', 'john@example.com', 'password1', 'ROLE_USER'),
('admin1', 'Admin', 'Smith', 'admin@example.com', 'adminpass1', 'ROLE_ADMIN');

-- Insertamos algunas prioridades
INSERT INTO priorities (name, value) VALUES 
('Low', 1),
('Medium', 2),
('High', 3);

-- Insertamos algunas tareas
INSERT INTO tasks (name, description, status, due_date, user_id, priority_id) VALUES 
('Task 1', 'Description for Task 1', 'Pending', '2024-02-25', 1, 1),
('Task 2', 'Description for Task 2', 'Completed', '2024-02-23', 2, 2);

-- Insertamos algunas etiquetas
INSERT INTO tags (name, description) VALUES 
('Work', 'Tasks related to work'),
('Personal', 'Personal tasks'),
('Urgent', 'High priority tasks');

-- Insertamos algunas relaciones entre tareas y etiquetas
INSERT INTO task_tag (task_id, tag_id) VALUES 
(1, 1), -- Task 1 is related to Work
(1, 2), -- Task 1 is also related to Personal
(2, 3); -- Task 2 is related to Urgent
