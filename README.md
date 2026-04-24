# 🔀 API Gateway

Escoge tu idioma / Choose your language:

<details open>
<summary><b>🇪🇸 Español</b></summary>

## Resumen

**API Gateway** es el punto de entrada único de la plataforma de torneos. Centraliza la autenticación JWT, el ruteo de requests y el control de acceso para todos los microservicios. Ningún cliente externo accede directamente a los servicios internos — todo el tráfico pasa por el gateway.

---

## Stack Tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 4.0.5 |
| Gateway | Spring Cloud Gateway MVC 2025.1.1 |
| Autenticación | JWT (jjwt 0.13.0) |
| Build Tool | Gradle |

---

## Arquitectura

```
Cliente (Postman / Frontend)
        │
        ▼
   API Gateway :8080
        │
        ├── JWTAuthFilter (valida token en cada request)
        │
        ├── /api/players/**  ──► Player Service :8081
        ├── /api/tournaments/** ──► Tournament Service :8082
        └── /api/rankings/**  ──► Ranking Service :8083
```

### Rutas Públicas

Las siguientes rutas no requieren autenticación:

- `POST /api/players/register`
- `POST /api/players/login`
- `/api/docs/**`
- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/webjars/**`

Cualquier otra ruta requiere un token JWT válido en el header `Authorization: Bearer <token>`.

---

## Primeros Pasos

### Requisitos

- Java 17+
- Gradle
- Los microservicios internos levantados (player, tournament, ranking)

### Ejecución local

1. Clonar el repositorio:
```bash
git clone https://github.com/NicoVelasco96/MS-Gateway.git
cd MS-Gateway
```

2. Configurar las variables de entorno (ver sección correspondiente).

3. Ejecutar la aplicación:
```bash
./gradlew bootRun
```

4. El gateway estará disponible en:
```
http://localhost:8080
```

---

## Variables de Entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `PLAYER_SERVICE_URL` | URL del player-service | `https://player-service.onrender.com` |
| `TOURNAMENT_SERVICE_URL` | URL del tournament-service | `https://tournament-service.onrender.com` |
| `RANKING_SERVICE_URL` | URL del ranking-service | `https://ranking-service.onrender.com` |
| `JWT_SECRET` | Clave secreta para validar tokens JWT | `tu_secret` |

> ⚠️ Nunca subas credenciales reales al repositorio. Siempre usá variables de entorno.
> ⚠️ El `JWT_SECRET` debe ser exactamente igual al configurado en el player-service.

---

## Endpoints

El gateway no expone endpoints propios — redirige todos los requests a los microservicios correspondientes según el prefijo de la URL.

| Prefijo | Servicio destino |
|---|---|
| `/api/players/**` | Player Service |
| `/api/tournaments/**` | Tournament Service |
| `/api/rankings/**` | Ranking Service |

### Ejemplo de uso

```bash
# Login (público)
POST http://localhost:8080/api/players/login
{
  "username": "NiicoV96",
  "password": "tupassword"
}

# Obtener torneo (requiere token)
GET http://localhost:8080/api/tournaments/1
Authorization: Bearer eyJhbGci...
```

---

## Flujo de Autenticación

```
Request entrante
        │
        ▼
JWTAuthFilter
        │
        ├── ¿Es ruta pública? ──► SÍ ──► Redirigir al servicio
        │
        └── NO
              │
              ▼
        ¿Tiene header Authorization: Bearer?
              │
              ├── NO ──► 401 Unauthorized
              │
              └── SÍ
                    │
                    ▼
              ¿Token válido?
                    │
                    ├── NO ──► 401 Unauthorized
                    │
                    └── SÍ ──► Redirigir al servicio destino
```

---

## Estructura del Proyecto

```
src/main/java/com/tournament/api_gateway/
├── config/
│   └── GatewayConfig.java              # Configuración de rutas
├── filter/
│   └── JWTAuthFilter.java              # Filtro de autenticación JWT
└── ApiGatewayApplication.java
```

</details>

---

<details>
<summary><b>🇺🇸 English</b></summary>

## Overview

**API Gateway** is the single entry point for the tournament platform. It centralizes JWT authentication, request routing, and access control for all microservices. No external client accesses the internal services directly — all traffic flows through the gateway.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.5 |
| Gateway | Spring Cloud Gateway MVC 2025.1.1 |
| Authentication | JWT (jjwt 0.13.0) |
| Build Tool | Gradle |

---

## Architecture

```
Client (Postman / Frontend)
        │
        ▼
   API Gateway :8080
        │
        ├── JWTAuthFilter (validates token on every request)
        │
        ├── /api/players/**  ──► Player Service :8081
        ├── /api/tournaments/** ──► Tournament Service :8082
        └── /api/rankings/**  ──► Ranking Service :8083
```

### Public Routes

The following routes do not require authentication:

- `POST /api/players/register`
- `POST /api/players/login`
- `/api/docs/**`
- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/webjars/**`

Any other route requires a valid JWT token in the `Authorization: Bearer <token>` header.

---

## Getting Started

### Prerequisites

- Java 17+
- Gradle
- Internal microservices running (player, tournament, ranking)

### Run locally

1. Clone the repository:
```bash
git clone https://github.com/NicoVelasco96/MS-Gateway.git
cd MS-Gateway
```

2. Set the required environment variables (see below).

3. Run the application:
```bash
./gradlew bootRun
```

4. The gateway will be available at:
```
http://localhost:8080
```

---

## Environment Variables

| Variable | Description | Example |
|---|---|---|
| `PLAYER_SERVICE_URL` | Player service URL | `https://player-service.onrender.com` |
| `TOURNAMENT_SERVICE_URL` | Tournament service URL | `https://tournament-service.onrender.com` |
| `RANKING_SERVICE_URL` | Ranking service URL | `https://ranking-service.onrender.com` |
| `JWT_SECRET` | Secret key for JWT validation | `your_secret` |

> ⚠️ Never commit credentials to the repository. Always use environment variables.
> ⚠️ The `JWT_SECRET` must be exactly the same as configured in the player-service.

---

## Endpoints

The gateway does not expose its own endpoints — it forwards all requests to the corresponding microservices based on the URL prefix.

| Prefix | Target Service |
|---|---|
| `/api/players/**` | Player Service |
| `/api/tournaments/**` | Tournament Service |
| `/api/rankings/**` | Ranking Service |

### Usage Example

```bash
# Login (public)
POST http://localhost:8080/api/players/login
{
  "username": "NiicoV96",
  "password": "yourpassword"
}

# Get tournament (requires token)
GET http://localhost:8080/api/tournaments/1
Authorization: Bearer eyJhbGci...
```

---

## Authentication Flow

```
Incoming request
        │
        ▼
JWTAuthFilter
        │
        ├── Is it a public route? ──► YES ──► Forward to service
        │
        └── NO
              │
              ▼
        Has Authorization: Bearer header?
              │
              ├── NO ──► 401 Unauthorized
              │
              └── YES
                    │
                    ▼
              Is token valid?
                    │
                    ├── NO ──► 401 Unauthorized
                    │
                    └── YES ──► Forward to target service
```

---

## Project Structure

```
src/main/java/com/tournament/api_gateway/
├── config/
│   └── GatewayConfig.java              # Route configuration
├── filter/
│   └── JWTAuthFilter.java              # JWT authentication filter
└── ApiGatewayApplication.java
```

</details>

---

## 📜 Licencia / License

Este proyecto es parte de un portafolio personal. / This project is part of a personal portfolio.
