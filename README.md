# Online Bookstore

A full-stack Online Bookstore application built using React and Spring Boot.

The application allows users to register and log in, browse books, manage a shopping cart, and place an order.

This project was developed as a technical assignment with an emphasis on clean, maintainable code and a clear separation of responsibilities between the frontend and backend.

---

## Features

### User Management

- Register a new user
- Login with email and password
- Password hashing using BCrypt
- Request validation

### Books

- Browse available books
- View title, author, and price
- Handle loading, empty, and error states

### Shopping Cart

- Add, update, and remove items
- Calculate cart totals
- Handle empty cart scenarios

### Checkout

- Review order summary
- Create an order from the current cart
- Clear the cart after successful checkout

---

## Architecture

The application is divided into separate frontend and backend applications.

```text
                    React Frontend
                          |
                          v
                      REST APIs
                          |
                          v
                     Spring Boot
                          |
          +---------------+---------------+
          |               |               |
     Controllers       Services       Security
          |               |
          +---------------+
                          |
                          v
                     Repositories
                          |
                          v
                    H2 Database
```

The frontend is responsible for the user interface and client-side state.

The backend handles business logic, validation, security, and data access.

---

## Project Structure

```text
online-bookstore
|
+-- frontend
|   +-- api
|   +-- components
|   +-- context
|   +-- pages
|
+-- backend
    +-- controller
    +-- dto
    +-- entity
    +-- exception
    +-- repository
    +-- security
    +-- service
```

---

## Technologies

### Frontend

- React
- JavaScript
- Axios

### Backend

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Maven

### Database

- H2 In-Memory Database

---

## Running the Application

### Prerequisites

- Java 17
- Maven
- Node.js
- npm

### Backend

```
cd online-bookstore\backend
mvn clean test
mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

### Frontend

```powershell
cd online-bookstore\frontend
```

Configure a `.env` file:

```text
VITE_API_URL=http://localhost:8080/api/v1
```

Run:

```powershell
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

### Frontend Checks

```powershell
npm run lint
npm run build
```

---

## API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

### Authentication

```http
POST /auth/register
POST /auth/login
```

### Books

```http
GET /books
```

### Cart

```http
GET    /cart
POST   /cart
PUT    /cart/{bookId}
DELETE /cart/{bookId}
```

### Orders

```http
POST /orders
```

---

## Validation and Error Handling

Bean Validation is used for request validation, and exceptions are handled centrally using:

```java
@RestControllerAdvice
```

Common scenarios include:

- Validation failures
- Invalid login attempts
- Missing books or cart items
- Empty cart checkout

---

## Security

Spring Security is configured for authentication-related flows.

Passwords are hashed using `BCryptPasswordEncoder` and are never returned in API responses.

JWT-based authorization is outside the scope of this assignment.

---

## Testing

```
mvn clean test
```

Unit tests cover the core business logic for authentication, cart operations, and order creation.

---

## Design Decisions

- Layered architecture (Controller → Service → Repository)
- DTOs used to separate API contracts from entities
- Constructor-based dependency injection
- React Context used for authentication and cart state management
- Centralized API communication using Axios

---

## Database

The application uses an H2 in-memory database, so no external database setup is required.

Data is reset when the application restarts.

---

## Assumptions and Trade-offs

This project was developed as a time-boxed technical assignment, with the focus on delivering the core bookstore workflow while keeping the implementation simple and maintainable.

- The cart is stored in memory and is not associated with individual users.
- Orders are created from the current cart but are not persisted.
- Registration and login are implemented, but JWT-based authorization is outside the current scope.
- No payment, admin, search, filtering, or pagination features are included.
- H2 is used as an in-memory database, so data is reset when the application restarts.

Passwords are hashed using BCrypt before being stored and are never stored in local storage.

In a production application, cart and order data would be persisted, associated with authenticated users, and protected using a more complete authorization mechanism.

---

## 