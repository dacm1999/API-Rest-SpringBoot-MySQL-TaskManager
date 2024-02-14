-- Creamos la base de datos
DROP DATABASE gestor_tasks;
CREATE DATABASE IF NOT EXISTS gestor_tasks;

-- Nos aseguramos de usar la base de datos que acabamos de crear
USE gestor_tasks;
DROP TABLE IF EXISTS `usuarios`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `prioridades`;
DROP TABLE IF EXISTS `tareas`;
DROP TABLE IF EXISTS `etiquetas`;

-- Creamos la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
	pw char(68) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Creamos la tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    rol VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES usuarios(user_id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Creamos la tabla de prioridades
CREATE TABLE IF NOT EXISTS prioridades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    valor INT NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Insertamos algunas prioridades
INSERT INTO prioridades (nombre, valor) VALUES 
('Baja', 1),
('Media', 2),
('Alta', 3);

-- Creamos la tabla de tareas
CREATE TABLE IF NOT EXISTS tareas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    estado ENUM('Pendiente', 'Completada') NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento DATE,
    user_id INT NOT NULL,
    prioridad_id INT,  -- Añadimos una columna para la relación con la tabla de prioridades
    FOREIGN KEY (user_id) REFERENCES usuarios(user_id) ON DELETE CASCADE,
    FOREIGN KEY (prioridad_id) REFERENCES prioridades(id)  -- Establecemos la relación con la tabla de prioridades
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Creamos la tabla de etiquetas
CREATE TABLE IF NOT EXISTS etiquetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Insertamos algunas etiquetas
INSERT INTO etiquetas (nombre) VALUES 
('Trabajo'),
('Personal'),
('Urgente');

-- Creamos la tabla de relación entre tareas y etiquetas
CREATE TABLE IF NOT EXISTS tarea_etiqueta (
    tarea_id INT,
    etiqueta_id INT,
    PRIMARY KEY (tarea_id, etiqueta_id),
    FOREIGN KEY (tarea_id) REFERENCES tareas(id) ON DELETE CASCADE,
    FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Agregamos una sentencia TRUNCATE TABLE para cada tabla
TRUNCATE TABLE tarea_etiqueta;
TRUNCATE TABLE roles;


