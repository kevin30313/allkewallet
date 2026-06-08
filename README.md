# 🚀 AlkeWallet - Plataforma de Banca Digital

AlkeWallet es una aplicación web de banca digital y billetera electrónica diseñada bajo una arquitectura moderna, segura y altamente escalable. El proyecto implementa un flujo completo de autenticación blindada y desacoplamiento de servicios, demostrando sólidas competencias en desarrollo Backend (Java/Spring Boot), Frontend (React) e Ingeniería de Infraestructura en la Nube (GCP).

---

## 🏗️ Arquitectura del Sistema

La plataforma está construida siguiendo los principios de **Clean Architecture** y separación de responsabilidades:

* **Frontend:** Aplicación SPA (Single Page Application) desarrollada con **React** y construida sobre **Vite** para un rendimiento óptimo en desarrollo y producción.
* **Backend:** Microservicios RESTful autónomos construidos con **Java 17** y **Spring Boot 3.x**.
* **Base de Datos:** Motor relacional **PostgreSQL** encargado de la persistencia robusta de usuarios, cuentas y transacciones.
* **Infraestructura Cloud:** Despliegue de servicios mediante contenedores Docker en **Google Cloud Platform (GCP)** utilizando **Cloud Run** para un escalado serverless eficiente.

---

## 🔒 Características Principales & Seguridad

* **Autenticación Robusta:** Flujo de seguridad implementado con **Spring Security** y **JWT (JSON Web Tokens)** para proteger los endpoints del negocio.
* **Interceptores Axios:** El frontend cuenta con un guardia de seguridad (`api.js`) que recupera automáticamente el token del `localStorage` y lo inyecta limpiamente en la cabecera `Authorization: Bearer <token>` antes de cada petición.
* **Políticas de CORS:** Configuración precisa del intercambio de recursos de origen cruzado para permitir una comunicación fluida y segura entre el entorno de ejecución del cliente y las API de GCP.
* **Diseño e Interfaz:** Experiencia de usuario inmersiva con una estética moderna de estilo "cyber-neon".

---

## 🛠️ Tecnologías Utilizadas

### Backend
* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.x (Spring Web, Spring Security, Spring Data JPA)
* **Seguridad:** JSON Web Tokens (JWT)
* **Base de Datos:** PostgreSQL / Hibernate

### Frontend
* **Biblioteca Principal:** React
* **Herramienta de Construcción:** Vite
* **Cliente HTTP:** Axios (con interceptores personalizados de petición)
* **Estilos:** CSS3 / Flexbox

### Infraestructura & DevOps
* **Cloud Provider:** Google Cloud Platform (GCP)
* **Servicio de Despliegue:** Cloud Run (Serverless Container Execution)
* **Contenedores:** Docker

---

## 🚀 Instalación y Ejecución Local

### Requisitos Previos
* Java 17 o superior instalado.
* Node.js (versión LTS recomendada) y npm.
* Instancia de PostgreSQL activa.

### Ejecución del Frontend
1. Navega al directorio del frontend:
```bash
   cd alkewallet-frontend
