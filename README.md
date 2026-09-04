# Online Bookstore

A full-stack Online Bookstore application built using React and Spring Boot.

The application allows users to register and log in, browse books, manage a shopping cart, and place an order.

This project was developed as a technical assignment with the focus on writing clean, maintainable code and keeping the frontend and backend responsibilities properly separated.

---

## Features

### User Management

- Register a new user
- Login with email and password
- Password hashing using BCrypt
- Request validation
- Meaningful validation and authentication error messages

### Books

- Fetch available books from the backend
- Display book title, author, and price
- Show loading, empty, and error states

### Shopping Cart

- Add books to cart
- View cart items
- Increase quantity
- Decrease quantity
- Remove items
- Calculate individual item totals
- Calculate the overall cart total
- Handle empty cart scenarios

### Checkout and Orders

- Display order summary
- Show item quantities and totals
- Prevent duplicate order submissions
- Create an order from the current cart
- Clear the cart after successful order creation
- Handle order success and failure scenarios

---

# Architecture

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
````

The frontend is responsible for the user interface and client-side state.

The backend handles business logic, validation, security configuration, and database operations.

---

# Backend Architecture

The backend follows a layered architecture:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Database
```

Each layer has a specific responsibility.

### Controller

Handles HTTP requests and responses.

Examples:

* `AuthController`
* `BookController`
* `CartController`
* `OrderController`

Controllers do not contain business logic. They delegate the work to service classes.

### Service

Contains the main business logic.

Examples:

* `AuthService`
* `BookService`
* `CartService`
* `OrderService`

For example, cart calculations and order creation are handled in the service layer rather than in the controller.

### Repository

Handles database access using Spring Data JPA.

Examples:

* `BookRepository`
* `UserRepository`

This keeps persistence logic separate from business logic.

---

# Project Structure

```text
online-bookstore
|
+-- frontend
|   |
|   +-- src
|       |
|       +-- api
|       |   +-- apiClient.js
|       |   +-- authApi.js
|       |   +-- bookApi.js
|       |   +-- cartApi.js
|       |   +-- orderApi.js
|       |
|       +-- components
|       |   +-- Navbar.jsx
|       |   +-- book
|       |       +-- BookCard.jsx
|       |
|       +-- context
|       |   +-- AuthContext.js
|       |   +-- AuthProvider.jsx
|       |   +-- CartContext.js
|       |   +-- CartProvider.jsx
|       |   +-- useAuth.js
|       |   +-- useCart.js
|       |
|       +-- pages
|       |   +-- LoginPage.jsx
|       |   +-- RegisterPage.jsx
|       |   +-- BooksPage.jsx
|       |   +-- CartPage.jsx
|       |   +-- CheckoutPage.jsx
|       |
|       +-- App.jsx
|       +-- main.jsx
|
+-- backend
    |
    +-- src/main/java/com/bookstore
    |   |
    |   +-- config
    |   +-- controller
    |   +-- dto
    |   +-- entity
    |   +-- exception
    |   +-- repository
    |   +-- security
    |   +-- service
    |
    +-- src/main/resources
    |   +-- application.properties
    |   +-- data.sql
    |
    +-- src/test
```

---

# Technologies Used

## Frontend

* React
* JavaScript
* Axios
* HTML/CSS
* ESLint

## Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* Maven
* Lombok

## Database

* H2 In-Memory Database

---

# Frontend Design

The frontend separates UI components, API communication, and shared application state.

## API Layer

Backend calls are kept in separate API modules:

```text
authApi.js
bookApi.js
cartApi.js
orderApi.js
```

Axios configuration is centralized in:

```text
apiClient.js
```

This avoids putting API calls directly inside every UI component.

---

## State Management

React Context is used for shared application state.

### AuthContext

Handles:

* Logged-in user
* Login
* Logout
* Authentication status

### CartContext

Handles:

* Cart items
* Cart total
* Loading state
* Add to cart
* Update quantity
* Remove item
* Refresh cart

---

# Backend Design Decisions

## DTO Pattern

DTOs are used for API requests and responses instead of directly exposing database entities.

Examples include:

```text
RegisterRequest
LoginRequest
UserResponse
BookResponse
AddToCartRequest
CartResponse
CartItemResponse
OrderResponse
```

This keeps the API model separate from the database model.

---

## Dependency Injection

Spring manages application dependencies using dependency injection.

Services receive their required dependencies through constructors instead of creating them manually.

For example:

```text
CartService -> BookRepository
OrderService -> CartService
AuthService -> UserRepository
```

---

## Separation of Concerns

The application keeps different responsibilities in separate classes and modules.

For example:

```text
Controller  -> HTTP handling
Service     -> Business logic
Repository  -> Database access
DTO         -> Request and response data
Context     -> Shared frontend state
API Module  -> Frontend HTTP communication
Component   -> UI rendering
```

This makes the code easier to understand, test, and modify.

---

# How to Run the Application

## Prerequisites

Make sure the following are installed:

* Java 17
* Maven
* Node.js
* npm

Verify the installations using:

```bash
java -version
mvn -version
node -v
npm -v
```

---

# Backend Setup

Navigate to the backend folder:

```powershell
cd online-bookstore\backend
```

Run the tests:

```powershell
mvn clean test
```

Start the backend:

```powershell
mvn spring-boot:run
```

The backend runs at:

```text
http://localhost:8080
```

---

# Frontend Setup

Open a new terminal and go to the frontend folder:

```powershell
cd online-bookstore\frontend
````

URL in a `.env` file:

```text
VITE_API_URL=http://localhost:8080/api/v1
```

Install dependencies and start the application:

```powershell
npm install
npm run dev
```

The frontend runs at:

```text
http://localhost:5173
```
---

# Frontend Checks

```
npm run lint
npm run build
```

---

# API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

---

### Authentication

**Register**

```http
POST /auth/register
```

```json
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123"
}
```

**Login**

```http
POST /auth/login
```

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

---

### Books

```http
GET /books
```

Returns the list of available books.

---

### Cart

| Action | Endpoint |
|---|---|
| Get cart | `GET /cart` |
| Add book | `POST /cart` |
| Update quantity | `PUT /cart/{bookId}` |
| Remove book | `DELETE /cart/{bookId}` |

Example add request:

```json
{
  "bookId": 1,
  "quantity": 2
}
```

---

### Orders

```http
POST /orders
```

Creates an order from the current cart. After a successful order, the cart is cleared.

Example response:

```json
{
  "orderId": 1001,
  "total": 1200.00,
  "status": "CONFIRMED"
}
```

# Validation and Error Handling

Input validation is handled using Bean Validation for fields such as email, password, book ID, and quantity.

Invalid requests return meaningful error messages.

## Exception Handling

Exceptions are handled centrally using:

```java
@RestControllerAdvice
````

This handles cases like:

* Book or cart item not found
* User already exists
* Invalid login
* Validation errors
* Empty cart during checkout
* Unexpected errors

---

# Security

Spring Security is configured for the application.

Passwords are encrypted using `BCryptPasswordEncoder` and are never returned in API responses.

Registration and login are implemented. JWT-based authorization is outside the scope of this assignment.

---

# Testing

Run tests:

```powershell
mvn clean test
```

---

# Code Quality

The project uses meaningful names and keeps methods focused on their responsibilities.

Examples:

```text
addToCart()
updateQuantity()
removeFromCart()
createOrder()

```

Comments are used only where they help explain important decisions or non-obvious logic.

---

## SOLID Principles

SOLID principles are applied where appropriate without over-engineering the project.

For example:

* Classes have focused responsibilities.
* Controllers and services have separate concerns.
* Database access is abstracted through repositories.
* Dependencies are injected rather than tightly coupled.
* DTOs separate API models from database entities.

The project is intentionally kept simple rather than adding unnecessary abstractions.

---

## REST API Design

The APIs follow standard REST conventions where appropriate.

Examples:

```text
GET     /books
GET     /cart
POST    /cart
PUT     /cart/{bookId}
DELETE  /cart/{bookId}
POST    /orders
```

The API uses HTTP status codes :

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
404 Not Found
500 Internal Server Error
````

---

# Configuration

The frontend API URL is configured using:

```text
VITE_API_URL
```

The backend CORS origin is configured through:

```properties
app.cors.allowed-origin
```
---

# Database

The application uses an H2 in-memory database, so no external database setup is required.

The data is reset when the application restarts. A production application would typically use PostgreSQL or MySQL.

---

# Assumptions and Trade-offs

This was developed as a time-boxed technical assignment, so the focus was on keeping the required functionality simple and maintainable.

* The cart is stored in memory rather than being linked to individual users.
* Orders are created from the current cart but are not persisted.
* Registration and login are implemented, but JWT-based authorization is outside the current scope.
* Passwords are hashed using BCrypt and are never stored in local storage.

In a production application, carts and orders need tp be persisted and associated with authenticated users.

---

# Known Limitations

To keep the assignment focused, a few features are outside the current scope:

- Cart is stored in memory and not linked to users.
- Orders are not persisted.
- No payment or admin functionality.
- No JWT-based authorization.
- No book management, search, filtering, or pagination.
- H2 data is lost when the application restarts.

---

# Future Improvements

```text
H2 Database          → Oracle/PostgreSQL
In-memory Cart       → Persistent user cart
In-memory Orders     → Order and OrderItem entities
Basic Login          → JWT authorization
No Payment           → Payment integration
No Pagination        → Pagination and filtering
````

The current structure provides a base for adding these features later.

---

# Application Flow

```text
Register
   ↓
Login
   ↓
View Books
   ↓
Add to Cart
   ↓
Update / Remove Items
   ↓
Checkout
   ↓
Place Order
   ↓
Order Confirmation
```

---

# Project Setup

Frontend and backend are kept separate for easier maintenance.

---

# Summary

This project demonstrates a bookstore flow using React and Spring Boot.

It includes:

* Layered backend architecture
* REST APIs and DTOs
* Centralized exception handling
* Password encryption
* React Context for shared state
* Reusable components
* Centralized API communication
* Unit tests for core business logic

The focus was on building the main functionality while keeping the code simple, readable, and maintainable.

```