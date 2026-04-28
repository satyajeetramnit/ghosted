# 👻 Ghosted Backend

The backend service for **Ghosted**, a comprehensive Job Application Tracking System designed to help users track applications, contacts, and interviews seamlessly.

## 🚀 Tech Stack

- **Framework**: Spring Boot 3.2.4
- **Language**: Java 25
- **Database**: PostgreSQL
- **Security**: Spring Security & JWT Authentication
- **ORM**: Spring Data JPA / Hibernate
- **Boilerplate Reduction**: Lombok 1.18.44

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your machine:
- **Java 25**
- **Maven** (optional, you can use the included wrapper)
- **PostgreSQL 16+**

## 🛠️ Local Development Setup

### 1. Database Setup
The application requires a PostgreSQL database named `ghosted_db`. If you are using Homebrew on macOS, you can set it up quickly:

```bash
# Start PostgreSQL service
brew services start postgresql@16

# Create a local 'postgres' user with superuser privileges (if you don't have one)
createuser -s postgres

# Set the password to 'postgres'
psql template1 -c "ALTER USER postgres PASSWORD 'postgres';"

# Create the database
createdb ghosted_db -O postgres
```

> The application connects using username `postgres` and password `postgres` by default. You can change this in `src/main/resources/application-dev.properties`.

### 2. Running the Application

You can start the backend server using the Maven Spring Boot plugin. Navigate to the project root directory and run:

```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8080` with the base context path set to `/api`.

### 3. Application Properties

The application uses profiles for environment-specific configurations:
- `application.properties`: Default settings and profile active selection (defaults to `dev`).
- `application-dev.properties`: Database credentials, Hibernate DDL settings (`update`), and logging levels for local development.
- `application-prod.properties`: Settings for production deployments.

## 🏗️ Project Structure

```
src/main/java/com/ghosted/
├── controller/     # REST API Endpoints
├── dto/            # Data Transfer Objects
├── entity/         # JPA Entities (Database models)
├── repository/     # Spring Data JPA Repositories
├── security/       # JWT Auth, UserDetails, Security Config
└── service/        # Business Logic layer
```

## 🔐 Authentication

This API is secured using **JSON Web Tokens (JWT)**.
- **Register**: `POST /api/auth/register`
- **Login**: `POST /api/auth/login` (Returns JWT token)

For protected endpoints, include the JWT token in the `Authorization` header:
```
Authorization: Bearer <your_jwt_token>
```