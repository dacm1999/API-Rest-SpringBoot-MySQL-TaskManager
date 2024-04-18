# Gestor de Tareas con Spring Boot y MySQL

Este proyecto es un gestor de tareas desarrollado con Spring Boot, una plataforma potente para el desarrollo de aplicaciones Java. Esta API RESTful fue creada utilizando la versión 17 de Java y permite a los usuarios crear, organizar y administrar sus tareas diarias de manera eficiente.

## Características principales

[<img src="00-images-cover/cover.png">](https://www.linkedin.com/in/dacm1/)

- **Autenticación de usuarios**: Los usuarios pueden registrarse e iniciar sesión de forma segura. Además, se envía un correo electrónico de registro exitoso al correo proporcionado.
- **Creación de tareas**: Los usuarios pueden crear nuevas tareas especificando su nombre, descripción, fecha de vencimiento y prioridad.
- **Organización de tareas**: Las tareas se pueden organizar por estado (pendiente o completada), fecha de vencimiento y prioridad.
- **Asignación de etiquetas**: Las tareas pueden etiquetarse para una mejor organización y clasificación.
- **Gestión de prioridades**: Las tareas pueden clasificarse por prioridad para identificar rápidamente las más importantes.
- **Recordatorios**: Los usuarios pueden establecer recordatorios para tareas importantes y recibir notificaciones en el momento adecuado.
- **Seguridad**: Se utiliza Spring Security para proteger la API y garantizar que solo los usuarios autorizados puedan acceder a sus tareas o a los diferentes endpoints según sus roles.
- **Persistencia de datos**: Se utiliza Spring Data JPA para interactuar con la base de datos MySQL y almacenar la información de las tareas y los usuarios de manera eficiente.

## Tecnologías utilizadas

- JWT 
- MySQL
- Lombok
- Spring Web
- Spring Boot
- Spring DevTools
- Spring Data JPA
- Spring Security
- Spring Starter Mail

- Postman
- IntelliJ Idea
- MySQL Workbench

## Instalación y configuración

Para ejecutar el proyecto localmente, sigue estos pasos:

1. Clona el repositorio desde GitHub utilizando el comando `git clone url-repositorio`.
2. Importa la base de datos MySQL y actualiza las credenciales en el archivo `application.properties` (La contraseña de todos los usuarios en la BD es 12345).
3. Compila el proyecto utilizando el comando Maven: `mvn clean install` o el mismo IDE.
4. Accede a la API desde tu navegador o herramienta de cliente REST, te recomiendo `Postman`.
5. Importa los archivos JSON que contienen todas las llamadas a la API para no tener que escrirlas. 

---

## Endpoints

### Autenticación
- **Login**: `POST /api/v1/auth/login`
- **Registro**: `POST /api/v1/auth/register`

### Usuarios
- **Obtener todos los usuarios**: `GET /api/v1/users/allUsers`
- **Crear usuario**: `POST /api/v1/users/`
- **Obtener usuario por ID**: `GET /api/v1/users/:id`
- **Actualizar usuario por ID**: `PUT /api/v1/users/:id`
- **Eliminar usuario por ID**: `DELETE /api/v1/users/:id`
- **Obtener usuario por nombre de usuario**: `GET /api/v1/users/username/:username`

### Gestión de Contraseñas
- **Restablecer contraseña**: `POST /api/v1/password/reset`
- **Establecer nueva contraseña**: `PUT /api/v1/password/newPassword/:username`

### Prioridades
- **Obtener prioridad por nombre**: `GET /api/v1/priorities/:name`
- **Obtener todas las prioridades**: `GET /api/v1/priorities/all`
- **Obtener prioridad por ID**: `GET /api/v1/priorities/id/:id`
- **Actualizar prioridad por ID**: `PUT /api/v1/priorities/:id`
- **Eliminar prioridad por ID**: `DELETE /api/v1/priorities/:id`
- **Crear una sola prioridad**: `POST /api/v1/priorities/single`
- **Crear múltiples prioridades**: `POST /api/v1/priorities/`

### Etiquetas
- **Obtener todas las etiquetas**: `GET /api/v1/tags/allTags`
- **Obtener etiqueta por nombre**: `GET /api/v1/tags/:tagName`
- **Obtener etiqueta por ID**: `GET /api/v1/tags/id/:id`
- **Crear una sola etiqueta**: `POST /api/v1/tags/single`
- **Eliminar etiqueta por ID**: `DELETE /api/v1/tags/:id`
- **Actualizar etiqueta por ID**: `PUT /api/v1/tags/:id`
- **Crear múltiples etiquetas**: `POST /api/v1/tags/`

### Tareas
- **Obtener tarea por ID**: `GET /api/v1/tasks/:id`
- **Obtener todas las tareas**: `GET /api/v1/tasks/all`
- **Obtener tareas por nombre de usuario**: `GET /api/v1/tasks/userTasks/:username`
- **Obtener tareas por ID de usuario**: `GET /api/v1/tasks/user/:id`
- **Actualizar tarea por ID**: `PUT /api/v1/tasks/:id`
- **Eliminar tarea por ID**: `DELETE /api/v1/tasks/:id`
- **Crear una sola tarea**: `POST /api/v1/tasks/single`
- **Crear múltiples tareas**: `POST /api/v1/tasks/`

---
