# SISTEMA BODEGA LEO API


Descripción
\- API para la gestión de la bodega "Bodega Leo". Proporciona endpoints REST para usuarios, inventario y operaciones de bodega.

Documentación
\- Interfaz Swagger disponible en: http://localhost:8080/api/swagger-ui.html


Requisitos
\- Java 17+
\- Maven
\- MySQL

Configuración
\- Editar `resources/application.yml` y configurar las propiedades de conexión a MySQL (usuario, contraseña, URL).
\- Variables típicas a ajustar: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`.

Base de datos
\- Crear la base de datos `bodegaLeoDB` en el servidor MySQL antes de ejecutar la aplicación.
\- Las tablas se generan automáticamente al iniciar la aplicación si está activada la configuración de DDL.

