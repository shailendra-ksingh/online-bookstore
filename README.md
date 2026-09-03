# Online Bookstore

A simple full-stack online bookstore application developed using React and Spring Boot.

The application demonstrates book listing, user registration/login, shopping cart management, and a basic checkout/order flow.

The implementation is intentionally scoped to the requirements of the two-day technical assignment.

---

## 1. Architecture

The application follows a simple layered architecture.

```text
                    React Frontend
                          |
                       REST APIs
                          |
                    Spring Boot
                          |
          +---------------+---------------+
          |               |               |
     Controllers       Services       Security
          |               |               |
          +---------------+---------------+
                          |
                     Repositories
                          |
                         H2
                          |
                       Database
```

### Frontend Structure

```text
Frontend
React
 |
 +-- Pages
 |    +-- Login
 |    +-- Register
 |    +-- Books
 |    +-- Cart
 |    +-- Checkout
 |
 +-- Components
 |
 +-- Context
 |    +-- Authentication
 |    +-- Cart
 |
 +-- API
      +-- Authentication
      +-- Books
```

### Backend Structure

```text
com.bookstore
 |
 +-- config
 |    +-- SecurityConfig
 |
 +-- controller
 |    +-- AuthController
 |    +-- BookController
 |    +-- CartController
 |    +-- OrderController
 |
 +-- dto
 |    +-- auth
 |    +-- book
 |    +-- cart
 |    +-- order
 |
 +-- entity
 |    +-- Book
 |    +-- User
 |
 +-- repository
 |    +-- BookRepository
 |    +-- UserRepository
 |
 +-- service
 |    +-- AuthService
 |    +-- BookService
 |    +-- CartService
 |    +-- OrderService
 |
 +-- security
 |    +-- CustomUserDetailsService
 |
 +-- exception
      +-- GlobalExceptionHandler
```

### Request Flow

```text
React UI
   |
   v
REST Controller
   |
   v
Service Layer
   |
   v
Repository
   |
   v
H2 Database
```

Controllers are responsible for handling HTTP requests, while business logic is kept in service classes.

---

## 2. Technologies

### Frontend

- React
- Vite
- JavaScript
- HTML/CSS
- Fetch API

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Maven

### Database

- H2 In-Memory Database

### Development Tools

- IntelliJ IDEA
- Node.js
- npm
- Maven

---

## 3. Features

### Book Management

- Retrieve available books
- Display book title, author and price
- Book data is stored in H2

### User Authentication

- User registration
- User login
- Passwords stored using BCrypt hashing
- Spring Security configuration for authentication

### Shopping Cart

- Add books to cart
- View cart
- Increase/decrease quantity from the frontend
- Remove books from cart
- Calculate item totals
- Calculate cart total

### Checkout

- Display order summary
- Display quantities and individual item totals
- Calculate final order total
- Place an order
- Display order ID, total and confirmation status

---

## 4. How to Run

### Prerequisites

Install:

- Java 21
- Maven
- Node.js
- npm

Verify installations:

```bash
java -version
mvn -version
node -v
npm -v
```

### Start Backend

Open PowerShell:

```powershell
cd online-bookstore\backend
```

Run tests:

```bash
mvn clean test
```

Start Spring Boot:

```bash
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

### Start Frontend

Open another PowerShell window:

```powershell
cd online-bookstore\frontend
```

Install dependencies:

```bash
npm install
```

Start React/Vite:

```bash
npm run dev
```

Frontend normally runs on:

```text
http://localhost:5173
```

If port 5173 is already in use, Vite may start on another port such as:

```text
http://localhost:5174
```

### Frontend Production Build

```bash
npm run build
```

---

## 5. API Endpoints

### Base URL

```text
http://localhost:8080
```

### Authentication

#### Register

```http
POST /api/v1/auth/register
```

Request:

```json
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123"
}
```

#### Login

```http
POST /api/v1/auth/login
```

Request:

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

### Books

#### Get Available Books

```http
GET /api/v1/books
```

Example Response:

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

### Shopping Cart

#### Add Book to Cart

```http
POST /api/v1/cart
```

Request:

```json
{
  "bookId": 1,
  "quantity": 2
}
```

#### Get Cart

```http
GET /api/v1/cart
```

Example Response:

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

#### Remove Book from Cart

```http
DELETE /api/v1/cart/{bookId}
```

Example:

```http
DELETE /api/v1/cart/1
```

### Orders

#### Create Order

```http
POST /api/v1/orders
```

The order is created using the current backend cart.

Example Response:

```json
{
  "orderId": 1001,
  "total": 1200.00,
  "status": "CONFIRMED"
}
```

---

## 6. Error Handling

A centralized exception handler is implemented using:

```java
@RestControllerAdvice
```

For example, attempting to place an order with an empty cart returns a **400 Bad Request**.

Example:

```json
{
  "status": 400,
  "message": "Cannot create order because cart is empty"
}
```

This keeps exception handling out of individual controllers and provides a consistent API response.

---

## 7. Clean Code Principles Followed

### Single Responsibility

- Controllers handle HTTP requests
- Services contain business logic
- Repositories handle database access
- DTOs represent API request/response data

### Dependency Injection

Spring constructor injection is used instead of manually creating dependencies.

Example:

```java
public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
}
```

### Separation of Concerns

```text
Controller
    |
    v
Service
    |
    v
Repository
```

This makes the code easier to test and maintain.

### Reusable Components

Common functionality is placed in services and reusable React components/context.

Examples:

- BookCard
- CartContext
- AuthContext
- BookService
- CartService
- OrderService

### DTOs

DTOs are used instead of exposing request/response structures directly through entities.

Examples:

- RegisterRequest
- LoginRequest
- BookResponse
- AddToCartRequest
- CartResponse
- OrderResponse

### Centralized Error Handling

Exceptions are handled centrally using:

```text
GlobalExceptionHandler
```

instead of duplicating try/catch logic across controllers.

### Validation

Basic validation is performed for values such as:

- Required book ID
- Quantity greater than zero
- Empty cart before creating an order

### Meaningful Naming

Classes and methods use names that describe their responsibilities.

Examples:

- CartService
- OrderService
- createOrder()
- addToCart()
- removeFromCart()
- getCart()

### Minimal Comments

Comments are used where they explain implementation decisions rather than describing obvious code.

---

## 8. Assumptions and Trade-offs

Because this was a two-day technical assignment, implementation decisions were made to prioritize the explicitly requested functionality.

### H2 Database

H2 was selected because it requires minimal configuration and is suitable for demonstrating database persistence during development.

A production deployment would typically use a persistent database such as MySQL or PostgreSQL.

### In-Memory Cart

The cart is currently maintained in memory.

This keeps the implementation small and demonstrates the required cart REST APIs.

In a production system, the cart would be persisted and associated with the authenticated user.

### Simplified Order Processing

The order API validates the cart, calculates the total on the backend and returns an order confirmation.

The current implementation does not persist orders.

This was a deliberate time-boxing decision.

### Server-Side Price Calculation

The backend calculates the order total using book prices from the backend/cart rather than trusting a total supplied by the frontend.

This prevents the frontend from being the source of truth for the final price.

### Simple Authentication

Basic registration and login were implemented using Spring Security.

JWT-based authentication is not implemented; basic authentication using Spring Security is provided as required by the assignment.

---

## 9. Known Limitations

The following are known limitations of the time-boxed implementation:

- Cart data is stored in memory and is not persistent.
- Cart data is not currently associated with individual users.
- Orders are not persisted in the database.
- No payment gateway is implemented.
- JWT/token-based authentication is not implemented.
- No admin functionality is included.
- No book create/update/delete functionality is included because only book retrieval was required.
- No search, filtering, or pagination is implemented.
- The frontend currently uses the local backend URL during development.
- H2 is an in-memory database, so data is lost when the application restarts.
- Production concerns such as distributed caching, monitoring, and centralized logging are outside the scope of this assignment.

---

## 10. Production Extension Plan

```text
Current                         Production Extension
----------------------------------------------------------------
H2                              MySQL/PostgreSQL
In-memory cart                  Persistent UserCart/UserCartItem
Non-persistent order            Order/OrderItem database entities
Basic authentication            JWT/OAuth2
Local API URL                   Environment-based configuration
Basic validation                Bean Validation
Basic error response            Standard API error model
No payment                      Payment gateway integration
No inventory                    Inventory management
No observability                Logging + metrics + tracing
```

The current architecture keeps these extensions possible without requiring the frontend to be completely redesigned.

---

## 11. Scope

The implementation focuses on the following requested business flow:

```text
Register
   |
   v
Login
   |
   v
View Books
   |
   v
Add Books to Cart
   |
   v
Modify / Remove Cart Items
   |
   v
Checkout
   |
   v
View Order Summary
   |
   v
Place Order
   |
   v
Order Confirmation
```

The project intentionally avoids implementing features outside the assignment scope in order to keep the solution maintainable and achievable within the two-day time constraint.