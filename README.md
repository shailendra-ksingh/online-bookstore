# Online Bookstore

A full-stack Online Bookstore application built using React and Spring Boot.

The application allows users to register and log in, browse books, manage a shopping cart, and place an order.

This project was developed as a technical assignment. The main focus was to keep the application simple, easy to understand, and maintainable.

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

- Add books to the cart
- Update item quantity
- Remove items from the cart
- Calculate cart totals
- Cart items are stored in the database
- Handle empty cart scenarios

### Checkout

- Review order summary
- Create an order from the current cart
- Store order and order item details
- Clear the cart after successful checkout

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
          |               |
          +---------------+
                          |
                          v
                     Repositories
                          |
                          v
                    H2 Database
````

The frontend handles the user interface and client-side state.

The backend handles the business logic, validation, authentication, and database access.

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

* React
* JavaScript
* Axios

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* Maven

### Database

* H2 In-Memory Database

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

## API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

## HTTP Status Codes

The API uses standard HTTP status codes, including:

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

Bean Validation is used for request validation.

Exceptions are handled centrally using:

```
@RestControllerAdvice
```

Some of the handled cases are:

* Invalid request data
* Invalid login
* Book not found
* Cart item not found
* Checkout with an empty cart

## Security

Spring Security is used for the login and authentication flow.

Passwords are hashed using `BCryptPasswordEncoder`.

Passwords are not returned in API responses.

JWT-based authorization is outside the scope of this assignment.

## Testing

Backend tests can be run using:

```text
mvn clean test
```

Unit tests cover the main business logic for:

* User registration and login
* Cart operations
* Order creation

## Design Decisions

* Layered architecture: Controller → Service → Repository
* DTOs are used for API requests and responses
* Constructor-based dependency injection is used
* React Context is used for authentication and cart state
* Axios is used for backend API calls
* Cart and order data are stored using JPA

## Database

The application uses an H2 in-memory database.

No external database setup is required.

The database is recreated when the application starts, so the data is reset when the application restarts.

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

### Repository

Repositories handle database operations using Spring Data JPA.

Examples include:

* `BookRepository`
* `UserRepository`
* `CartRepository`
* `CartItemRepository`
* `OrderRepository`
* `OrderItemRepository`

### DTO

DTOs are used for API request and response data instead of directly exposing entity objects.

### Exception Handling

Common application exceptions are handled in one place using `@RestControllerAdvice`.

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

Axios configuration is kept in `apiClient.js`.

This keeps API calls separate from the UI code.

## Assumptions and Trade-offs

This was a time-limited technical assignment, so some parts were kept simple.

* The cart is stored in the database and contains multiple cart items.
* A single default cart is used instead of creating a separate cart for each user.
* Orders and order items are stored in the database.
* Basic registration and login are implemented.
* JWT or OAuth authentication is not included.
* Payment processing is not included.
* Admin functionality is not included.
* Search, filtering, and pagination are not included.
* H2 is used as an in-memory database, so data is reset when the application restarts.
* Order creation is transactional. The order is saved and the cart is cleared as part of the checkout operation.

The single default cart was chosen to keep the implementation within the scope of the assignment.

In a production application, carts and orders would normally be linked to individual users and a more complete authorization solution would be used.

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
   v
Create Order
   |
   v
Clear Cart
```

The project keeps the frontend and backend separate and uses Spring Boot REST APIs for communication between them.

```