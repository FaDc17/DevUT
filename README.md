# DEVUT

Aplicación web DEVUT migrada a Spring Boot 3, Spring MVC, Thymeleaf, Spring Data JPA y Hibernate.

## Ejecución local

La configuración predeterminada utiliza H2 en memoria para que la interfaz pueda ejecutarse sin Azure SQL:

```bash
mvn spring-boot:run
```

Abrir `http://localhost:8080`. Acceso administrativo de prueba: `admin` / `123456`.

## Azure SQL

Completar las variables de `.env.example`, cargar el archivo de variables en el entorno y configurar las propiedades SQL Server. Las migraciones de esquema están deshabilitadas (`ddl-auto=none`). No se crean tablas ni datos automáticamente.
