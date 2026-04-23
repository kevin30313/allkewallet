# AlkeWallet - Backend API 💰

AlkeWallet es una API REST robusta diseñada para gestionar operaciones de una billetera digital. Este proyecto forma parte de mi portafolio profesional como Desarrollador Backend, enfocándose en la seguridad, escalabilidad y buenas prácticas de arquitectura.

## 🚀 Tecnologías Utilizadas

* **Java 17** & **Spring Boot 3.x**
* **Spring Security & JWT (JSON Web Tokens)**: Implementación de seguridad Stateless para autenticación de usuarios.
* **JPA / Hibernate**: Gestión de persistencia de datos.
* **PostgreSQL**: Base de datos relacional (desplegada en Render).
* **Maven**: Gestión de dependencias.
* **Docker**: Contenerización para despliegue consistente.

## 🔐 Seguridad e Infraestructura

La API cuenta con una arquitectura de seguridad avanzada:
* **Autenticación JWT**: Filtro de interceptación de peticiones (`OncePerRequestFilter`) para validar tokens en cada solicitud.
* **Cifrado de contraseñas**: Uso de `BCryptPasswordEncoder` para proteger los datos sensibles.
* **Despliegue Continuo (CD)**: Configurado en **Render** con integración directa desde este repositorio.
* **Arquitectura Cloud**: Preparado para entornos AWS gracias a su diseño sin estado (Stateless).

## 🛠️ Estructura del Proyecto

```text
src/main/java/com/kevin30313/alkewallet/
├── config/        # Configuración de Seguridad y Filtros JWT
├── controller/    # Endpoints de la API (Auth, Users, etc.)
├── dto/           # Objetos de Transferencia de Datos (Login/Register)
├── model/         # Entidades de la Base de Datos
├── repository/    # Interfaces para acceso a datos
└── service/       # Lógica de negocio y manejo de JWT
