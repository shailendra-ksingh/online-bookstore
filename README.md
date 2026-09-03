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
                          v
                      REST APIs
                          |
                          v
                     Spring Boot
                          |
          +---------------+---------------+
          |               |               |
     Controllers       Services       Security
          |               |               |
          +---------------+---------------+
                          |
                          v
                     Repositories
                          |
                          v
                    H2 Database
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
 |    +-- BookCard
 |
 +-- Context
 |    +-- Authentication
 |    +-- Cart
 |
 +-- API
      +-- Authentication
      +-- Books
      +-- Cart
      +-- Orders
```

The frontend uses React Context for shared authentication and cart state.

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

Controllers handle HTTP requests, while business logic is kept in service classes.

---

## 2. Technologies

### Frontend

* React
* Vite
* JavaScript
* HTML/CSS
* Axios

### Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* Maven

### Database

* H2 In-Memory Database

### Development Tools

* IntelliJ IDEA
* Node.js
* npm
* Maven

---

## 3. Features

### Book Management

* Retrieve available books
* Display book title, author and price
* Book data is stored in H2

### User Authentication

* User registration
* User login
* Passwords stored using BCrypt hashing
* Spring Security configuration for authentication

### Shopping Cart

* Add books to cart
* View cart
* Increase/decrease quantity
* Remove books from cart
* Calculate item totals
* Calculate cart total
* Backend returns calculated cart totals to the frontend

### Checkout

* Display order summary
* Display quantities and individual item totals
* Display final cart total
* Place an order
* Display order ID and confirmation status
* Refresh cart state after successful order placement

---

## 4. How to Run

### Prerequisites

Install:

* Java 21
* Maven
* Node.js
* npm

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

If port 5173 is already in use, Vite may start on another available port.

### Frontend Quality Checks

Run ESLint:

```bash
npm run lint
```

Create a production build:

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

#### Update Cart Quantity

```http
PUT /api/v1/cart/{bookId}
```

Request:

```json
{
  "quantity": 3
}
```

Example:

```http
PUT /api/v1/cart/1
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

For example, attempting to place an order with an empty cart returns a `400 Bad Request`.

Example:

```json
{
  "status": 400,
  "message": "Cannot create order because cart is empty"
}
```

This keeps exception handling separate from controllers and provides consistent error responses.

On the frontend, API errors are caught and displayed to the user where appropriate, such as login, registration, cart operations, and checkout.

---

## 7. Clean Code Principles Followed

### Separation of Responsibilities

* Controllers handle HTTP requests
* Services contain business logic
* Repositories handle database access
* DTOs represent API request/response data
* React Context manages shared frontend state
* API modules keep HTTP calls separate from UI components

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

This keeps responsibilities separated and makes the code easier to understand and maintain.

### Reusable Components

Common functionality is placed in reusable services, React components, and contexts.

Examples:

* BookCard
* CartContext
* AuthContext
* BookService
* CartService
* OrderService

### DTOs

DTOs are used for API request and response objects.

Examples:

* RegisterRequest
* LoginRequest
* BookResponse
* AddToCartRequest
* CartResponse
* CartItemResponse
* OrderResponse

### Centralized Error Handling

Backend exceptions are handled centrally using:

```text
GlobalExceptionHandler
```

This avoids repeating exception handling logic in individual controllers.

### Validation

Basic validation is performed for values such as:

* Required book ID
* Quantity greater than zero
* Empty cart before creating an order

### Meaningful Naming

Classes and methods use names based on their responsibilities.

Examples:

* CartService
* OrderService
* createOrder()
* addToCart()
* removeFromCart()
* getCart()

### Minimal Comments

Comments are mainly used to explain decisions or non-obvious logic rather than obvious code.

---

## 8. Assumptions and Trade-offs

Because this was a two-day technical assignment, the implementation focuses on the required functionality and keeps the design intentionally simple.

### H2 Database

H2 was selected because it requires minimal configuration and is suitable for demonstrating database functionality during development.

A production application would typically use a persistent database such as MySQL or PostgreSQL.

### In-Memory Cart

The cart is currently maintained in memory.

This keeps the implementation small and focuses on demonstrating the required cart REST APIs.

In a production application, the cart would normally be persisted and associated with the authenticated user.

### Simplified Order Processing

The order API validates the cart, calculates the total on the backend, and returns an order confirmation.

The current implementation does not persist orders.

This was kept simple due to the time-boxed nature of the assignment.

### Server-Side Price Calculation

The backend calculates the final order total using backend book/cart data rather than trusting a total sent by the frontend.

This ensures the backend remains the source of truth for pricing.

### Simple Authentication

Basic registration and login are implemented using Spring Security.

JWT/token-based authentication is not included because the assignment was kept intentionally small and focused on the required functionality.

---

## 9. Known Limitations

The following limitations are known in the current implementation:

* Cart data is stored in memory and is not persistent.
* Cart data is not currently associated with individual users.
* Orders are not persisted in the database.
* No payment gateway is implemented.
* JWT/token-based authentication is not implemented.
* No admin functionality is included.
* No book create/update/delete functionality is included because only book retrieval was required.
* No search, filtering, or pagination is implemented.
* The frontend is configured for local development.
* H2 is an in-memory database, so data is lost when the application restarts.
* Production concerns such as distributed caching, monitoring, and centralized logging are outside the scope of this assignment.

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
Basic validation                More comprehensive Bean Validation
Basic error response            Standard API error model
No payment                      Payment gateway integration
No inventory                    Inventory management
No observability                Logging + metrics + tracing
```

The current separation between frontend, API, service, and repository layers makes these extensions possible without major changes to the overall structure.

---

## 11. Scope

The implementation focuses on the following business flow:

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

The project intentionally avoids adding features outside the assignment scope in order to keep the implementation focused and achievable within the three-day time constraint.

