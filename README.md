# LibroLoop

Sistema de gestión de biblioteca desarrollado con Java 21 y Spring Boot 3.x.

## Descripción

LibroLoop es una aplicación backend para la gestión completa de una biblioteca, incluyendo:
- Gestión de catálogo (libros, autores, categorías, ejemplares)
- Gestión de socios/usuarios
- Sistema de préstamos con cálculo automático de fechas y multas
- Sistema de reservas con cola de espera
- Autenticación y autorización basada en roles (bibliotecario/socio)
- Notificaciones simuladas

## Stack Tecnológico

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL 16** - Base de datos relacional
- **Maven** - Gestión de dependencias
- **Lombok** - Reducción de código boilerplate
- **Spring Validation** - Validación de datos
- **Docker Compose** - Orquestación de contenedores

## Requisitos Previos

- Java 21 instalado
- Maven 3.x instalado
- Docker y Docker Compose instalados

## Setup del Proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/Pablosky2806/libroloop.git
cd libroloop
```

### 2. Levantar la base de datos con Docker Compose

```bash
docker-compose up -d
```

Esto iniciará un contenedor de PostgreSQL en el puerto 5432 con las siguientes credenciales:
- Base de datos: `libroloop`
- Usuario: `libroloop`
- Password: `libroloop123`

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### 4. Verificar que la aplicación está funcionando

```bash
curl http://localhost:8080/actuator/health
```

## Estructura del Proyecto

```
libroloop/
├── src/
│   ├── main/
│   │   ├── java/com/libroloop/
│   │   │   └── LibroloopApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/libroloop/
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Configuración de la Base de Datos

La configuración de la base de datos se encuentra en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/libroloop
spring.datasource.username=libroloop
spring.datasource.password=libroloop123
```

## Desarrollo

El proyecto sigue una arquitectura en capas:
- **Controller**: Exposición de endpoints REST
- **Service**: Lógica de negocio
- **Repository**: Acceso a datos con Spring Data JPA
- **Entity**: Modelos de datos
- **DTO**: Objetos de transferencia de datos

## Estado del Proyecto

🚧 **En desarrollo** - Este proyecto está siendo construido incrementalmente.

## Autor

Desarrollado por [Pablo Contreras](https://github.com/Pablosky2806)

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
