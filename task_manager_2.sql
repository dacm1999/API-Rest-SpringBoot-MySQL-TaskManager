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