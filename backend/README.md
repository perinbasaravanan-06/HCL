# Food Ordering App API Documentation

This document provides detailed documentation for the Food Ordering App API. You can use the provided Postman collection to test the endpoints.

## Postman Collection

[Download Postman Collection](postman_collection.json)

To use the Postman collection, you will need to set the `baseUrl` and `authToken` variables in your Postman environment.

- `baseUrl`: The base URL of your application (e.g., `http://localhost:8080`)
- `authToken`: The JWT token you receive after authenticating.

## Authentication

To get an `authToken`, you need to register a new user and then log in.

### Register

- **Endpoint:** `POST /api/auth/register`
- **Body:**

```json
{
  "username": "testuser",
  "password": "password",
  "email": "testuser@example.com",
  "fullName": "Test User"
}
```

### Login

- **Endpoint:** `POST /api/auth/login`
- **Body:**

```json
{
  "username": "testuser",
  "password": "password"
}
```

The response from the login endpoint will contain the `authToken`.

## API Endpoints

### Products

- **Get All Products:** `GET /api/products`
- **Get Product by ID:** `GET /api/products/{id}`
- **Get Products by Category:** `GET /api/products/category/{category}`

### Cart

- **Get Cart:** `GET /api/cart` (Requires authentication)
- **Add to Cart:** `POST /api/cart/add` (Requires authentication)
  - **Body:**

  ```json
  {
    "productId": 1,
    "quantity": 1
  }
  ```

### Orders

- **Place Order:** `POST /api/orders` (Requires authentication)
  - **Body:**

  ```json
  {
    "paymentMethod": "COD",
    "couponCode": "SAVE20"
  }
  ```
