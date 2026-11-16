# Hospital Management System – Spring Boot & JWT

## Project Overview

Hospital Management System is a backend REST API application developed using **Java** and **Spring Boot**. The application provides secure user authentication and patient record management through RESTful APIs.

The project uses **Spring Security** and **JSON Web Tokens (JWT)** to authenticate users and protect patient management endpoints. **Spring Data JPA** is used for database operations, while **MySQL** stores user credentials and patient information.

The application follows a layered architecture that separates controllers, services, repositories, entities, DTOs, and security configuration to maintain clean, modular, and maintainable code.

The system provides two primary modules:

- **Authentication Module:** User registration, login, password encryption, JWT generation, and token-based authentication.
- **Patient Management Module:** Adding, retrieving, updating, and deleting patient records through secured REST APIs.

---

## Project Objectives

- Develop a RESTful backend application using Spring Boot.
- Implement user registration and login functionality.
- Securely store user passwords using BCrypt password hashing.
- Generate and validate JWT tokens for authentication.
- Implement role-based authentication information using Spring Security.
- Perform patient management operations using CRUD APIs.
- Integrate MySQL using Spring Data JPA and Hibernate.
- Implement layered architecture for better code organization.
- Test business logic using JUnit 5 and Mockito.
- Verify REST APIs using Postman.

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Spring Boot | Backend application framework |
| Spring Web | REST API development |
| Spring Security | Authentication and API security |
| JWT | Token-based authentication |
| Spring Data JPA | Database interaction |
| Hibernate | ORM implementation |
| MySQL | Relational database |
| Maven | Dependency and build management |
| Lombok | Boilerplate code reduction |
| Jakarta Validation | Request data validation |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies during testing |
| Postman | REST API testing |
| IntelliJ IDEA | Development environment |

---

## System Architecture

The application follows a layered architecture where each layer has a specific responsibility.

### 1. Controller Layer

Handles incoming HTTP requests and returns appropriate HTTP responses. It connects the client with the service layer.

**Controllers:** `AuthController`, `PatientController`

**Responsibilities:**
- Accept HTTP requests.
- Read request bodies and path variables.
- Perform request validation using Jakarta Validation.
- Call the appropriate service methods.
- Return HTTP responses with appropriate status codes.

### 2. Service Layer

Contains the application's business logic.

**Services:** `AuthService`, `PatientService`

**Responsibilities:**
- Register new users.
- Check for duplicate email addresses.
- Encrypt passwords before saving them.
- Authenticate users during login.
- Generate JWT tokens after successful authentication.
- Perform patient CRUD operations.
- Convert entities into DTOs and DTOs into entities.
- Handle records that do not exist.

### 3. Repository Layer

Communicates with the MySQL database using Spring Data JPA.

**Repositories:** `UserRepository`, `PatientRepository`

**Responsibilities:**
- Save, retrieve, update, and delete records.
- Find users using their email addresses.
- Check whether an email address is already registered.

Both repositories extend `JpaRepository`, which provides commonly used database operations without requiring manual SQL queries for basic CRUD functionality.

### 4. Entity Layer

Represents database tables.

**Entities:** `User`, `Patient`, `Role` (Enum)

The entities are mapped to database tables using Jakarta Persistence annotations.

### 5. DTO Layer

DTO stands for Data Transfer Object. DTOs transfer data between the client and the application without directly exposing database entities.

**DTO classes:** `AuthRequest`, `AuthResponse`, `PatientDto`

### 6. Security and Configuration Layer

Manages authentication, authorization, JWT operations, and application security.

**Classes:** `SecurityConfig`, `JwtAuthenticationFilter`, `JwtService`

**Responsibilities:**
- Configure Spring Security.
- Allow public access to authentication endpoints.
- Protect patient management endpoints.
- Generate JWT tokens.
- Extract user information from tokens.
- Validate token signatures and expiration.
- Authenticate requests using the JWT authentication filter.
- Maintain stateless session management.

---

## Project Structure

```text
hospital-management-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── hms/
│   │   │               │
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── JwtService.java
│   │   │               │   └── JwtAuthenticationFilter.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   └── PatientController.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── AuthRequest.java
│   │   │               │   ├── AuthResponse.java
│   │   │               │   └── PatientDto.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   ├── User.java
│   │   │               │   ├── Patient.java
│   │   │               │   └── Role.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   └── PatientRepository.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── AuthService.java
│   │   │               │   └── PatientService.java
│   │   │               │
│   │   │               └── HospitalManagementSystemApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── hms/
│                       ├── config/
│                       │   ├── JwtServiceTest.java
│                       │   └── JwtAuthenticationFilterTest.java
│                       │
│                       ├── controller/
│                       │   ├── AuthControllerTest.java
│                       │   └── PatientControllerTest.java
│                       │
│                       └── service/
│                           ├── AuthServiceTest.java
│                           └── PatientServiceTest.java
│
├── pom.xml
└── README.md
```

---

## Application Modules

### Module 1: Authentication

#### User Registration

**Endpoint:** `POST /api/auth/register`

The user provides: Name, Email, Password.

**Registration workflow:**
1. The client sends registration details through a POST request.
2. The controller receives and validates the request.
3. The service checks whether the email already exists.
4. The password is encrypted using BCrypt.
5. A new user entity is created.
6. The default `USER` role is assigned.
7. The user details are saved in MySQL.
8. A success response is returned.

**Example request:**

```json
{
    "name": "Admin User",
    "email": "admin@gmail.com",
    "password": "Admin@123"
}
```

**Expected response:** `User registered successfully`

**HTTP Status:** `201 Created`

#### User Login

**Endpoint:** `POST /api/auth/login`

**Login workflow:**
1. The client sends the registered email and password.
2. The service searches for the user using the email address.
3. The submitted password is verified against the stored BCrypt hash.
4. If authentication succeeds, the JWT service generates a token.
5. The token is returned to the client.

**Example request:**

```json
{
    "email": "admin@gmail.com",
    "password": "Admin@123"
}
```

**Example response:**

```json
{
    "token": "generated-jwt-token"
}
```

**HTTP Status:** `200 OK`

If the email or password is incorrect, authentication fails.

---

### Module 2: Patient Management

The patient management module provides CRUD functionality (Create, Read, Update, Delete). **All patient endpoints require a valid JWT token.**

#### 1. Add Patient

**Endpoint:** `POST /api/patients`

Adds a new patient record to the database.

```json
{
    "name": "Rahul Sharma",
    "age": 25,
    "gender": "Male",
    "disease": "Fever",
    "address": "Kolkata",
    "phoneNumber": "9876543210"
}
```

**Expected response:** Patient details with the generated ID.
**HTTP Status:** `201 Created`

#### 2. Get All Patients

**Endpoint:** `GET /api/patients`

Retrieves all patient records stored in the database.
**HTTP Status:** `200 OK`

#### 3. Get Patient By ID

**Endpoint:** `GET /api/patients/{id}`

Retrieves an individual patient using their unique database ID. Example: `GET /api/patients/1`
**HTTP Status:** `200 OK`

#### 4. Update Patient

**Endpoint:** `PUT /api/patients/{id}`

Updates the details of an existing patient. Example: `PUT /api/patients/1`

```json
{
    "name": "Rahul Sharma Updated",
    "age": 26,
    "gender": "Male",
    "disease": "Recovered",
    "address": "Kolkata",
    "phoneNumber": "9876543210"
}
```

**HTTP Status:** `200 OK`

#### 5. Delete Patient

**Endpoint:** `DELETE /api/patients/{id}`

Deletes a patient record using its unique ID. Example: `DELETE
