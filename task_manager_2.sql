-- Creamos la base de datos
DROP DATABASE gestor_tasks;
CREATE DATABASE IF NOT EXISTS gestor_tasks;

-- Nos aseguramos de usar la base de datos que acabamos de crear
USE gestor_tasks;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `priorities`;
DROP TABLE IF EXISTS `tareas`;
DROP TABLE IF EXISTS `tags`;

-- Creamos la tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    pw CHAR(68) NOT NULL,
    role VARCHAR(50) NOT NULL,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO users (username, firstname, lastname, email, pw, role)
VALUES
    ('user3', 'Michael', 'Johnson', 'michael.johnson@example.com', 'password3', 'ROLE_ADMIN'),
    ('user4', 'Emily', 'Davis', 'emily.davis@example.com', 'password4', 'ROLE_USER'),
    ('user5', 'James', 'Wilson', 'james.wilson@example.com', 'password5', 'ROLE_USER'),
    ('user6', 'Emma', 'Martinez', 'emma.martinez@example.com', 'password6', 'ROLE_ADMIN'),
    ('user7', 'William', 'Anderson', 'william.anderson@example.com', 'password7', 'ROLE_USER'),
    ('user8', 'Olivia', 'Taylor', 'olivia.taylor@example.com', 'password8', 'ROLE_USER'),
    ('user9', 'Benjamin', 'Thomas', 'benjamin.thomas@example.com', 'password9', 'ROLE_ADMIN'),
    ('user10', 'Isabella', 'Hernandez', 'isabella.hernandez@example.com', 'password10', 'ROLE_USER'),
    ('user11', 'Mason', 'Moore', 'mason.moore@example.com', 'password11', 'ROLE_USER'),
    ('user12', 'Sophia', 'Martin', 'sophia.martin@example.com', 'password12', 'ROLE_ADMIN'),
    ('user13', 'Alexander', 'Jackson', 'alexander.jackson@example.com', 'password13', 'ROLE_USER'),
    ('user14', 'Charlotte', 'Thompson', 'charlotte.thompson@example.com', 'password14', 'ROLE_USER'),
    ('user15', 'Ethan', 'Garcia', 'ethan.garcia@example.com', 'password15', 'ROLE_ADMIN'),
    ('user16', 'Amelia', 'White', 'amelia.white@example.com', 'password16', 'ROLE_USER'),
    ('user17', 'Daniel', 'Clark', 'daniel.clark@example.com', 'password17', 'ROLE_USER'),
    ('user18', 'Ava', 'Rodriguez', 'ava.rodriguez@example.com', 'password18', 'ROLE_ADMIN'),
    ('user19', 'Logan', 'Lewis', 'logan.lewis@example.com', 'password19', 'ROLE_USER'),
    ('user20', 'Harper', 'Walker', 'harper.walker@example.com', 'password20', 'ROLE_USER'),
    ('user21', 'Sebastian', 'Hall', 'sebastian.hall@example.com', 'password21', 'ROLE_ADMIN'),
    ('user22', 'Mia', 'Young', 'mia.young@example.com', 'password22', 'ROLE_USER'),
    ('user23', 'Elijah', 'Allen', 'elijah.allen@example.com', 'password23', 'ROLE_USER'),
    ('user24', 'Evelyn', 'Scott', 'evelyn.scott@example.com', 'password24', 'ROLE_ADMIN'),
    ('user25', 'Carter', 'Adams', 'carter.adams@example.com', 'password25', 'ROLE_USER'),
    ('user26', 'Abigail', 'Green', 'abigail.green@example.com', 'password26', 'ROLE_USER'),
    ('user27', 'Jack', 'Baker', 'jack.baker@example.com', 'password27', 'ROLE_ADMIN'),
    ('user28', 'Madison', 'Nelson', 'madison.nelson@example.com', 'password28', 'ROLE_USER'),
    ('user29', 'Luke', 'Hill', 'luke.hill@example.com', 'password29', 'ROLE_USER'),
    ('user30', 'Lily', 'Rivera', 'lily.rivera@example.com', 'password30', 'ROLE_ADMIN'),
    ('user31', 'Gabriel', 'Campbell', 'gabriel.campbell@example.com', 'password31', 'ROLE_USER'),
    ('user32', 'Grace', 'Mitchell', 'grace.mitchell@example.com', 'password32', 'ROLE_USER'),
    ('user33', 'Ryan', 'Perez', 'ryan.perez@example.com', 'password33', 'ROLE_ADMIN'),
    ('user34', 'Nora', 'Roberts', 'nora.roberts@example.com', 'password34', 'ROLE_USER'),
    ('user35', 'Samuel', 'Turner', 'samuel.turner@example.com', 'password35', 'ROLE_USER'),
    ('user36', 'Hannah', 'Phillips', 'hannah.phillips@example.com', 'password36', 'ROLE_ADMIN'),
    ('user37', 'David', 'Russell', 'david.russell@example.com', 'password37', 'ROLE_USER'),
    ('user38', 'Addison', 'Gomez', 'addison.gomez@example.com', 'password38', 'ROLE_USER'),
    ('user39', 'Henry', 'King', 'henry.king@example.com', 'password39', 'ROLE_ADMIN'),
    ('user40', 'Scarlett', 'Cook', 'scarlett.cook@example.com', 'password40', 'ROLE_USER'),
    ('user41', 'Wyatt', 'Carter', 'wyatt.carter@example.com', 'password41', 'ROLE_USER'),
    ('user42', 'Zoey', 'Parker', 'zoey.parker@example.com', 'password42', 'ROLE_ADMIN'),
    ('user43', 'Matthew', 'Gray', 'matthew.gray@example.com', 'password43', 'ROLE_USER'),
    ('user44', 'Audrey', 'James', 'audrey.james@example.com', 'password44', 'ROLE_USER'),
    ('user45', 'Christopher', 'Morris', 'christopher.morris@example.com', 'password45', 'ROLE_ADMIN'),
    ('user46', 'Avery', 'Stewart', 'avery.stewart@example.com', 'password46', 'ROLE_USER'),
    ('user47', 'Oliver', 'Cooper', 'oliver.cooper@example.com', 'password47', 'ROLE_USER'),
    ('user48', 'Aria', 'Morgan', 'aria.morgan@example.com', 'password48', 'ROLE_ADMIN'),
    ('user49', 'Eli', 'Reyes', 'eli.reyes@example.com', 'password49', 'ROLE_USER'),
    ('user50', 'Leah', 'Lee', 'leah.lee@example.com', 'password50', 'ROLE_USER');



-- Creamos la tabla de prioridades
CREATE TABLE IF NOT EXISTS priorities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    value INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Creamos la tabla de tareas
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    status ENUM('Pending', 'Completed') NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date DATE,
    user_id INT NOT NULL,
    priority_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (priority_id) REFERENCES priorities(id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Creamos la tabla de etiquetas
CREATE TABLE IF NOT EXISTS Tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description varchar(200) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Creamos la tabla de relación entre tareas y etiquetas
CREATE TABLE IF NOT EXISTS task_tag (
    task_id INT,
    tag_id INT,
    PRIMARY KEY (task_id, tag_id),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Agregamos una sentencia TRUNCATE TABLE para cada tabla
TRUNCATE TABLE task_tag;