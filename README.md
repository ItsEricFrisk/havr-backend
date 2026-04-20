# Havr - Backend

A fullstack product organizer — track what you have, what you need, and what you want.
Built with Spring Boot, JWT authentication, and PostgreSQL.

## Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL (production) / H2 (development)
- Springdoc OpenAPI (Swagger UI)
- JUnit 6, Mockito, MockMvc

## Getting Started

### Prerequisites

- Java 21
- Maven

### Setup

1. Clone the repository
```bash
   git clone https://github.com/itsericfrisk/havr-backend.git
   cd havr-backend
```

2. Create `src/main/resources/application-dev.properties`:
```properties
   spring.datasource.url=jdbc:h2:mem:havrdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=sa
   spring.jpa.defer-datasource-initialization=true
   spring.sql.init.mode=always
   spring.jpa.hibernate.ddl-auto=create-drop
   jwt.secret=REPLACE_WITH_YOUR_SECRET_MIN_32_CHARS
```

3. Create `src/test/resources/application-test.properties`:
```properties
   spring.datasource.url=jdbc:h2:mem:havrdb;DB_CLOSE_DELAY=-1
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=sa
   spring.jpa.hibernate.ddl-auto=create-drop
   spring.jpa.show-sql=true
   spring.sql.init.mode=never
   spring.jpa.defer-datasource-initialization=false
   jwt.secret=REPLACE_WITH_YOUR_SECRET_MIN_32_CHARS
```

4. Run the application
```bash
   ./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

## Environment Variables

| Variable | Description |
|----------|-------------|
| `JWT_SECRET` | Secret key used to sign JWT tokens (min 32 chars) |

## Running Tests

```bash
./mvnw test
```

## API Documentation

Swagger UI is available at `/swagger-ui/index.html` when the application is running.

## CI/CD

GitHub Actions runs all tests on every push and pull request to `main`.
