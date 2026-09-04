# Online Bookstore

A full-stack Online Bookstore application built using React and Spring Boot.

The application allows users to register and log in, browse books, manage a shopping cart, and place an order.

This project was developed as a technical assignment with an emphasis on clean, maintainable code and a clear separation of responsibilities between the frontend and backend.


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

## Running the Application

### Prerequisites

- Java 17
- Maven
- Node.js and npm

### 1. Start the Backend

Open a terminal and run:

```
cd online-bookstore\backend
mvn spring-boot:run
````

The backend starts at:

```text
http://localhost:8080
```

To run backend tests:

```
mvn clean test
```

### 2. Start the Frontend

Open another terminal and run:

```powershell
cd online-bookstore\frontend
```

Configure/verify a `.env` file with:

```text
VITE_API_URL=http://localhost:8080/api/v1
```

Then install dependencies and start the application:

```powershell
npm install
npm run dev
```

Open the application at:

```text
http://localhost:5173
```

### Optional Frontend Checks

```powershell
npm run lint
npm run build
```

## API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

## HTTP Status Codes

The API uses appropriate HTTP status codes, including:

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
404 Not Found
500 Internal Server Error
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


## Database

The application uses an H2 in-memory database, so no external database setup is required.

Data is reset when the application restarts.


## Code Quality, Separation of Concerns

The project follows common clean code and design practices to keep the application easy to understand and maintain.

Frontend and backend responsibilities are clearly separated.

On the backend:

- Controllers handle HTTP requests and responses.
- Services contain the business logic.
- Repositories handle database access.
- DTOs are used for API request and response models.
- Exception handling is centralized using `@RestControllerAdvice`.

On the frontend:

- Pages focus on UI and user interactions.
- Context manages shared application state such as authentication and cart data.
- API modules handle communication with backend endpoints.
- Reusable components are kept separate from page-level logic.

### SOLID Principles

SOLID principles are applied where appropriate without adding unnecessary abstractions.

- Classes and components have focused responsibilities. For example, `CartService` handles cart operations, while `OrderService` handles order creation.
- Dependencies are injected using constructor injection, keeping components loosely coupled and easier to test.
- The layered structure makes it easier to add new features without heavily affecting unrelated parts of the application.

### Good Practices

- Meaningful and consistent naming
- Small, focused methods
- Constructor-based dependency injection
- DTOs instead of exposing entities directly
- Centralized exception handling
- Request validation on the backend
- Error handling on both frontend and backend
- Reusable React components
- React Context for shared state
- API communication separated from UI components
- Password hashing using BCrypt
- Environment-based configuration for API URL and CORS settings

## Assumptions and Trade-offs

This project was developed as a time-boxed technical assignment, with the focus on delivering the core bookstore workflow while keeping the implementation simple and maintainable.

- The cart is stored in memory and is not associated with individual users.
- Orders are created from the current cart but are not persisted.
- Basic user authentication (registration and login) is implemented as part of the assignment scope.
- JWT-based authorization or OAuth authentication is not included in the current scope.
- Payment processing, admin functionality, search, filtering, and pagination are not included.
- Transaction management is not implemented
- H2 is used as an in-memory database, so data is reset when the application restarts.

Passwords are hashed using BCrypt before being stored and are never stored in local storage.

In a production application, cart and order data would be persisted, associated with authenticated users, and protected using a more complete authorization mechanism.
