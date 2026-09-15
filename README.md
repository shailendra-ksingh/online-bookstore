# Online Bookstore

A full-stack Online Bookstore application built using React and Spring Boot.

The application allows users to register and log in, browse books, manage a shopping cart, and complete checkout to create an order.

This project was developed as a technical assignment. The main focus was to keep the application simple and maintainable while also demonstrating practical Java, Spring Boot, OOP, SOLID, and design-pattern concepts.

## Features

### User Management

- Register a new user
- Login with email and password
- Password hashing using BCrypt
- Request validation
- Centralized exception handling

### Books

- Browse available books
- View title, author, price, and stock
- Validate stock during checkout
- Update inventory after successful checkout
- Optimistic locking using JPA `@Version`

### Shopping Cart

- Add books to the cart
- Update item quantity
- Remove items from the cart
- Calculate cart totals
- Cart and cart items are stored in the database
- Handle empty cart scenarios

### Checkout and Orders

- Review the current cart
- Validate that the cart is not empty
- Validate book stock before checkout
- Process payment through a payment abstraction
- Create an order and order items
- Update inventory
- Clear the cart after successful checkout
- Use an enum for order status
- Keep checkout database operations within a transaction

### Payment

A real payment gateway is outside the scope of this assignment.

A simulated payment service is used to demonstrate payment abstraction and the Strategy design pattern.

Supported payment methods:

- Card
- UPI

Each payment method has its own strategy implementation.

---

## Architecture

The frontend and backend are separate applications.

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
                          |
                          v
                    Domain Entities
                          |
                          v
                     Repositories
                          |
                          v
                    H2 Database
````

The frontend handles the user interface, client-side state, validation, and API communication.

The backend handles business logic, validation, authentication, checkout, inventory management, payment abstraction, and database access.

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

* React
* JavaScript
* Axios
* Vite

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* Maven
* Lombok

### Database

* H2 In-Memory Database

### Testing

* JUnit 5
* Mockito
* Spring Test

---

## Running the Application

### Prerequisites

* Java 17
* Maven
* Node.js and npm

### 1. Start the Backend

Open a terminal and run:

```text
cd online-bookstore\backend
mvn spring-boot:run
```

The backend starts at:

```text
http://localhost:8080
```

To run backend tests:

```text
mvn clean test
```

### 2. Start the Frontend

Open another terminal and run:

```powershell
cd online-bookstore\frontend
```

Configure or check the `.env` file:

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

## HTTP Status Codes

The API uses standard HTTP status codes, including:

```text
200 OK
201 Created
400 Bad Request
401 Unauthorized
404 Not Found
409 Conflict
500 Internal Server Error
```

`409 Conflict` is used when the requested operation conflicts with the current inventory state, such as insufficient stock during checkout.

---

## Validation and Error Handling

Bean Validation is used for request validation.

Application exceptions are handled centrally using:

```text
@RestControllerAdvice
```

Some of the handled cases include:

* Invalid request data
* Invalid login
* Book not found
* Cart item not found
* Empty cart during checkout
* Insufficient stock
* Invalid payment request

The API returns an appropriate HTTP status and error response for these cases.

---

## Security

Spring Security is used for the login and authentication flow.

Passwords are hashed using `BCryptPasswordEncoder`.

Passwords are not returned in API responses.

JWT-based authorization is outside the scope of this assignment.

For the assignment, cart and order operations use a simplified application flow. In a production application, these operations would be associated with the authenticated user and protected accordingly.

---

## Domain Model

The backend uses domain entities for the main bookstore concepts.

```text
Cart
 |
 +-- CartItem
       |
       +-- Book


Order
 |
 +-- OrderItem
       |
       +-- Book information
```

The main entities are:

* `Book`
* `Cart`
* `CartItem`
* `Order`
* `OrderItem`
* `User`

Cart and order relationships are managed using JPA.

This keeps the main business objects represented in the domain model instead of keeping all cart and order information only in DTOs or in-memory structures.

---

## Object-Oriented Design

The implementation demonstrates the main OOP concepts where they are useful for the application.

### Encapsulation

Business rules that belong to an entity are kept close to that entity.

For example, stock reduction is handled by the `Book` entity:

```java
book.reduceStock(quantity);
```

The method checks the requested quantity and prevents stock from being reduced below the available amount.

`CartItem` also provides domain methods such as:

```java
increaseQuantity(...)
updateQuantity(...)
```

This keeps quantity-related rules inside the entity rather than directly changing the field from different places.

### Abstraction

Payment processing is exposed through the `PaymentService` interface.

The checkout service does not need to know the details of a particular payment implementation.

### Polymorphism

The payment implementations use the same `PaymentStrategy` interface.

```text
PaymentStrategy
      |
      +-- CardPaymentStrategy
      |
      +-- UpiPaymentStrategy
```

The payment service works with the common abstraction while the individual strategies provide the actual payment behavior.

---

## Design Patterns

The project uses a few design patterns where they are useful for the application.

### Builder Pattern

The Builder pattern is used for constructing entities such as:

* `Cart`
* `CartItem`
* `Order`
* `OrderItem`

For example:

```java
Order order = Order.builder()
        .totalAmount(total)
        .status(OrderStatus.CONFIRMED)
        .build();
```

This makes object construction easier to read when several fields are involved.

### Factory Method

Order creation is kept together through the order creation method:

```java
Order.fromCart(cart)
```

This keeps the creation of the `Order` and its `OrderItem` objects in one place instead of putting all construction details in the service.

### Strategy Pattern

Payment processing uses the Strategy pattern because payment method is a natural variation point.

```text
PaymentStrategy
      |
      +-- CardPaymentStrategy
      |
      +-- UpiPaymentStrategy
```

The checkout flow depends on the payment abstraction. If another payment method is required later, another strategy can be added without changing the main checkout flow.

### Dependency Injection

Spring constructor injection is used for service dependencies.

Services receive their dependencies from Spring rather than creating them directly. This also makes the classes easier to test using Mockito.

Spring's default singleton bean scope is used where appropriate; a manual Singleton implementation was not necessary for this application.

---

## Checkout Flow

The checkout process follows a simple sequence:

```text
Get Cart
   |
   v
Check Cart is not Empty
   |
   v
Validate Stock for All Items
   |
   v
Process Payment
   |
   v
Create Order
   |
   v
Reduce Stock
   |
   v
Save Order
   |
   v
Clear Cart
```

The checkout operation is transactional:

```java
@Transactional
public OrderResponse createOrder()
```

The database changes involved in order creation, inventory update, and cart clearing are therefore handled within the same transaction.

Payment is simulated for this assignment. A real external payment gateway would need additional handling for payment failures, retries, and idempotency because an external payment operation cannot be rolled back by a database transaction.

---

## Inventory and Concurrency

Stock availability is checked before creating the order.

For example, if the cart requests 5 books but only 2 are available, checkout is rejected with a `409 Conflict` response.

The `Book` entity also uses JPA optimistic locking:

```java
@Version
private Long version;
```

The application does not manually manage this field. JPA/Hibernate uses it when updating the entity and can detect when another transaction has modified the same book in the meantime.

This provides a basic safeguard against concurrent inventory updates.

For a production system with higher concurrency, additional mechanisms such as stock reservation, retry handling, or idempotency could be considered depending on the business requirements.

---

## Logging

Logging is added around important business operations, including:

* Add to cart
* Update cart item
* Remove from cart
* Clear cart
* Checkout start and completion
* Stock validation failures
* Payment processing
* Order creation

Sensitive information such as passwords, tokens, and payment credentials is not logged.

---

## Testing

Backend tests can be run using:

```text
mvn clean test
```

The tests cover the main business flows as well as important failure scenarios.

### Authentication

* User registration
* Duplicate user registration
* Login
* Invalid credentials

### Books

* Book retrieval
* Book not found
* Stock reduction
* Insufficient stock

### Cart

* Add book to cart
* Add the same book again
* Add multiple books
* Calculate cart total
* Update quantity
* Remove item
* Missing book
* Missing cart item

### Orders and Checkout

* Create order from cart
* Create order with correct order items
* Empty cart checkout
* Insufficient stock during checkout
* Payment failure
* Cart clearing after successful checkout

### Payment

* Card payment strategy
* UPI payment strategy
* Invalid payment amount

The tests use Mockito for service-level unit testing and JUnit 5 for test execution and assertions.

The focus is on covering the main success paths and the failure cases that are important to the business flow.

This was a time-limited assignment, so the implementation was not treated as a strict red-green-refactor TDD exercise for every feature.

---

## Code Structure

The backend follows a simple layered structure.

### Controller

Controllers handle HTTP requests and responses.

Examples:

* `AuthController`
* `BookController`
* `CartController`
* `OrderController`

### Service

Services contain the main business logic.

Examples:

* `AuthService`
* `BookService`
* `CartService`
* `OrderService`
* `PaymentService`

### Repository

Repositories handle database operations using Spring Data JPA.

Examples include:

* `BookRepository`
* `UserRepository`
* `CartRepository`
* `CartItemRepository`
* `OrderRepository`
* `OrderItemRepository`

### Entity

Entities represent the main domain objects and their relationships.

Examples:

* `Book`
* `Cart`
* `CartItem`
* `Order`
* `OrderItem`
* `User`

### DTO

DTOs are used for API request and response data instead of directly exposing entity objects.

### Exception Handling

Common application exceptions are handled in one place using:

```text
@RestControllerAdvice
```

---

## Frontend Structure

The frontend is split into pages, components, contexts, and API modules.

### Pages

The main pages are:

* Login
* Register
* Books
* Cart
* Checkout

### Context

React Context is used for shared application state.

`AuthContext` handles authentication state.

`CartContext` handles cart items, cart total, and cart operations.

### API

Backend calls are kept in separate API files:

* `authApi.js`
* `bookApi.js`
* `cartApi.js`
* `orderApi.js`

Axios configuration is kept in:

```text
apiClient.js
```

This keeps API calls separate from the UI code.

---

## Database

The application uses an H2 in-memory database.

No external database setup is required.

The database is recreated when the application starts, so the data is reset when the application restarts.

The main persisted data includes:

* Users
* Books
* Carts
* Cart items
* Orders
* Order items

---

## Assumptions and Trade-offs

This was a time-limited technical assignment, so some parts were intentionally kept simple.

* A single default cart is used instead of creating a separate cart for each user.
* Cart and order data are stored using JPA.
* Basic registration and login are implemented.
* JWT or OAuth authorization is not included.
* Payment gateway integration is not included.
* Payment is simulated using Card and UPI Strategy implementations.
* Admin functionality is not included.
* Search, filtering, and pagination are not included.
* H2 is used as an in-memory database, so data is reset when the application restarts.
* Checkout is transactional for the database operations.
* Inventory uses JPA optimistic locking to detect concurrent updates.

The simplified approach was chosen to keep the implementation within the scope and time constraints of the assignment.

In a production application, carts and orders would normally be linked to individual users, authorization would be stronger, and a production database and real payment integration would be used.

---

## SOLID Principles in the Implementation

The project uses SOLID principles where they fit naturally in the design.

### Single Responsibility Principle

The main classes have focused responsibilities:

* Controllers handle HTTP requests and responses.
* Services contain business logic.
* Repositories handle persistence.
* Payment strategies handle payment-specific behavior.

### Open/Closed Principle

The payment strategy design allows another payment method to be introduced without changing the main checkout orchestration.

For example, a new payment implementation can implement `PaymentStrategy` and be registered with the payment service.

### Liskov Substitution Principle

`CardPaymentStrategy` and `UpiPaymentStrategy` implement the same `PaymentStrategy` contract and can be used through that abstraction.

### Interface Segregation Principle

The application uses focused service interfaces rather than one large interface containing unrelated operations.

This keeps dependencies relatively small and easier to understand.

### Dependency Inversion Principle

The service layer depends on abstractions such as `PaymentService` rather than directly creating a concrete payment implementation.

Spring dependency injection is used to provide the required implementations.

---

## Summary

The application covers the main bookstore flow:

```text
Register
   |
   v
Login
   |
   v
Browse Books
   |
   v
Add Books to Cart
   |
   v
Update Cart
   |
   v
Checkout
   |
   +--> Validate Stock
   |
   +--> Process Payment
   |
   +--> Create Order
   |
   +--> Update Inventory
   |
   v
Clear Cart
```

The project keeps the frontend and backend separate and uses Spring Boot REST APIs for communication between them.

## Key Technical Concepts

The backend uses the following concepts and practices:

- OOP concepts, Encapsulation, Abstraction and polymorphism
- SOLID principles
- Builder pattern, Factory Method, Strategy pattern
- Spring dependency injection
- JPA relationships
- Transaction management, Optimistic locking
- Validation and exception handling
- Logging, Unit testing