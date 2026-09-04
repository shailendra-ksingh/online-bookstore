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
* Vite
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

This keeps shared application state separate from individual page components.

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

Open another terminal and navigate to the frontend folder:

```powershell
cd online-bookstore\frontend
```

Create a `.env` file in the frontend root directory:

```text
VITE_API_URL=http://localhost:8080/api/v1
```

Install dependencies:

```powershell
npm install
```

Start the application:

```powershell
npm run dev
```

The frontend normally runs at:

```text
http://localhost:5173
```

If port `5173` is already in use, Vite may use another available port.

---

# Frontend Quality Checks

Run ESLint:

```powershell
npm run lint
```

Create a production build:

```powershell
npm run build
```
---

# API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

---

## Authentication

### Register User

```http
POST /auth/register
```

Example request:

```json
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123"
}
```

A successful registration returns:

```text
201 Created
```

---

### Login

```http
POST /auth/login
```

Example request:

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

Invalid credentials return:

```text
401 Unauthorized
```

---

## Books

### Get Books

```http
GET /books
```

Example response:

```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "price": 500.00
  }
]
```

---

## Cart

### Add Book to Cart

```http
POST /cart
```

Example request:

```json
{
  "bookId": 1,
  "quantity": 2
}
```

---

### Get Cart

```http
GET /cart
```

Example response:

```json
{
  "items": [
    {
      "bookId": 1,
      "title": "Clean Code",
      "price": 500.00,
      "quantity": 2,
      "itemTotal": 1000.00
    }
  ],
  "total": 1000.00
}
```

---

### Update Cart Quantity

```http
PUT /cart/{bookId}
```

Example:

```http
PUT /cart/1
```

Request:

```json
{
  "quantity": 3
}
```

---

### Remove Cart Item

```http
DELETE /cart/{bookId}
```

Example:

```http
DELETE /cart/1
```

---

## Orders

### Create Order

```http
POST /orders
```

The order is created using the current cart.

Example response:

```json
{
  "orderId": 1001,
  "total": 1200.00,
  "status": "CONFIRMED"
}
```

After a successful order:

1. The current cart is read.
2. The order response is created.
3. The backend cart is cleared.
4. The frontend refreshes its cart state.
5. The user sees the order confirmation.

---

# Validation and Error Handling

Request validation is implemented using Bean Validation annotations.

Examples include:

* Required fields
* Valid email format
* Password length validation
* Required book ID
* Positive book ID
* Quantity greater than zero

Invalid requests return meaningful validation errors.

---

## Centralized Exception Handling

Exception handling is centralized using:

```java
@RestControllerAdvice
```

The application handles scenarios such as:

* Book not found
* Cart item not found
* User already exists
* Invalid login credentials
* Request validation errors
* Empty cart during checkout
* Unexpected server errors

This avoids repeating exception handling code across controllers and keeps error responses consistent.

---

# Security

Spring Security is configured for the application.

Passwords are never stored as plain text.

`BCryptPasswordEncoder` is used to hash passwords before storing them in the database.

Passwords are also not returned in API responses.

The application currently supports registration and login, but JWT or session-based API authorization is outside the scope of this assignment.

In a production application, protected endpoints would normally use JWT, OAuth2, or another secure authentication mechanism.

---

# Testing

The backend includes tests for important business logic and common edge cases.

The test coverage includes areas such as:

### Authentication

* Successful registration
* Duplicate email handling
* Successful login
* Invalid credentials

### Books

* Fetching available books
* Empty book list
* Entity to DTO mapping

### Cart

* Adding books
* Increasing quantity
* Updating quantity
* Removing items
* Calculating totals
* Missing book scenarios
* Updating a cart item that does not exist

### Orders

* Successful order creation
* Empty cart validation
* Order ID generation

Run all backend tests using:

```powershell
mvn clean test
```

---

# Code Quality

The project follows a few clean coding principles.

## Clean and Readable Code

The code uses meaningful names and tries to keep methods focused on one responsibility.

Examples:

```text
addToCart()
updateQuantity()
removeFromCart()
createOrder()
loadCart()
handlePlaceOrder()
```

The intention is to make most of the code understandable without requiring excessive comments.

Comments are mainly used for important decisions or non-obvious implementation details.

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

Appropriate HTTP status codes are used for common scenarios such as:

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
404 Not Found
409 Conflict
500 Internal Server Error
```

---

# Configuration

The frontend API URL is configured using an environment variable:

```text
VITE_API_URL
```

The backend CORS origin is configured through application properties:

```properties
app.cors.allowed-origin
```

Keeping these values outside the main application logic makes configuration changes easier across environments.

---

# Database

The application uses an H2 in-memory database.

H2 was chosen because it keeps the project easy to run without requiring an external database installation.

The database is recreated when the application restarts.

For a production application, PostgreSQL or MySQL would be more suitable.

---

# Assumptions and Trade-offs

This project was developed as a time-boxed technical assignment.

The main goal was to implement the required application flow while keeping the code simple and maintainable.

Some production-level features were intentionally kept outside the current scope.

---

## In-Memory Cart

The shopping cart is currently stored in memory.

This keeps the cart implementation straightforward for the assignment.

In a production application, cart data would normally be persisted and associated with an authenticated user.

---

## Simplified Order Processing

Orders are currently not persisted in the database.

The order flow is:

1. Get the current cart.
2. Validate that the cart is not empty.
3. Create an order response.
4. Clear the cart.

A production implementation would normally have `Order` and `OrderItem` entities stored in a database.

---

## Authentication Scope

User registration and login are implemented.

Passwords are securely hashed in the backend using BCrypt.

JWT or session-based authorization is not implemented as part of the current assignment.

The frontend stores basic user information only to maintain UI login state. Passwords are never stored in local storage.

---

# Known Limitations

The current implementation intentionally has the following limitations:

* Cart data is stored in memory.
* Cart data is not associated with individual users.
* Orders are not persisted.
* No payment gateway is implemented.
* JWT or token-based authorization is not implemented.
* No admin functionality is included.
* Book create, update, and delete APIs are not included.
* Search, filtering, and pagination are not implemented.
* H2 data is lost when the application restarts.

These limitations were kept outside the scope so the project could focus on the main bookstore flow.

---

# Possible Future Improvements

```text
Current Implementation              Possible Improvement
----------------------------------------------------------------
H2 database                         PostgreSQL / MySQL
In-memory cart                      Persistent user-specific cart
In-memory order                     Order and OrderItem entities
Basic login                         JWT / OAuth2 authorization
No payment                          Payment gateway integration
No inventory                        Inventory management
No pagination                       Pagination and filtering
Basic logging                       Monitoring and structured logging
```

The existing separation between controllers, services, repositories, DTOs, frontend contexts, and API modules provides a good base for these improvements.

---

# Application Flow

The main user journey is:

```text
Register
   |
   v
Login
   |
   v
View Available Books
   |
   v
Add Books to Cart
   |
   v
Update / Remove Cart Items
   |
   v
Checkout
   |
   v
Review Order Summary
   |
   v
Place Order
   |
   v
Order Confirmation
```

---

# Git and Project Setup

The repository includes a `.gitignore` configuration to avoid committing generated and environment-specific files such as:

* `node_modules`
* Build output
* IDE configuration files
* Environment files
* Compiled application files

The project structure keeps frontend and backend code independent, making changes easier to manage and reducing unnecessary coupling between unrelated parts of the application.

---

# Summary

This project demonstrates a frontend-to-backend application flow using React and Spring Boot.

It includes:

* Clean project structure
* Layered backend architecture
* Separation of concerns
* Constructor-based dependency injection
* DTO-based request and response models
* Spring Data JPA repositories
* Centralized exception handling
* RESTful API design
* Spring Security configuration
* BCrypt password encryption
* React Context for shared state
* Reusable React components
* Centralized Axios API communication

The implementation focuses on the core requirements while keeping the code readable, modular, and easy to extend.

```
