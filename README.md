# Gestor de Tareas con Spring Boot

Este proyecto es un gestor de tareas desarrollado con Spring Boot, una potente plataforma para la creación de aplicaciones Java. El gestor de tareas permite a los usuarios crear, organizar y administrar sus tareas diarias de manera eficiente.

## Características principales

- **Autenticación de usuarios**: Los usuarios pueden registrarse, iniciar sesión y gestionar sus tareas de forma segura.
- **Creación de tareas**: Los usuarios pueden crear nuevas tareas especificando su nombre, descripción, fecha de vencimiento y prioridad.
- **Organización de tareas**: Las tareas se pueden organizar por estado (pendiente o completada), fecha de vencimiento y prioridad.
- **Asignación de etiquetas**: Las tareas pueden etiquetarse para una mejor organización y clasificación.
- **Gestión de prioridades**: Las tareas pueden clasificarse por prioridad para identificar rápidamente las más importantes.
- **Recordatorios**: Los usuarios pueden establecer recordatorios para tareas importantes y recibir notificaciones en el momento adecuado.
- **Seguridad**: Se utiliza Spring Security para proteger la API y garantizar que solo los usuarios autorizados puedan acceder a sus tareas.
- **Persistencia de datos**: Se utiliza Spring Data JPA para interactuar con la base de datos MySQL y almacenar la información de las tareas y los usuarios de manera eficiente.

## Tecnologías utilizadas

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL
- Lombok

## Instalación y configuración

Para ejecutar el proyecto localmente, sigue estos pasos:

1. Clona el repositorio desde GitHub.
2. Configura la base de datos MySQL y actualiza las credenciales en el archivo `application.properties`.
3. Compila el proyecto utilizando Maven: `mvn clean install`.
4. Ejecuta la aplicación: `java -jar target/nombre-del-archivo.jar`.
5. Accede a la API desde tu navegador o herramienta de cliente REST favorita.

---

Puedes personalizar esta descripción según los detalles específicos de tu proyecto y agregar cualquier otra información relevante que desees destacar. ¡Espero que te sea útil!