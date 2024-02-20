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
    username VARCHAR(100) NOT NULL UNIQUE,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    pw CHAR(68) NOT NULL,
    role VARCHAR(50) NOT NULL,
	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


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
CREATE TABLE IF NOT EXISTS Tags (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description varchar(200) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Insertamos algunas etiquetas
INSERT INTO TAGS (name, description) VALUES 
('Trabajo','Tareas del trabajo'),
('Personal','Tareas personales'),
('Urgente','Primera providad');

-- Creamos la tabla de relación entre tareas y etiquetas
CREATE TABLE IF NOT EXISTS tarea_etiqueta (
    tarea_id INT,
    etiqueta_id INT,
    PRIMARY KEY (tarea_id, etiqueta_id),
    FOREIGN KEY (tarea_id) REFERENCES tareas(id) ON DELETE CASCADE,
    FOREIGN KEY (etiqueta_id) REFERENCES tags(id) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Agregamos una sentencia TRUNCATE TABLE para cada tabla
TRUNCATE TABLE tarea_etiqueta;