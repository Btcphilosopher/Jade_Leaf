# Jade Leaf 茶馆 - Enterprise REST API Documentation

This document describes the core endpoints for the **Jade Leaf** enterprise backend, designed to support national mobile operations with high concurrency, offline synchronization caching, and instant state pushes.

---

## Base URLs
* **Production**: `https://api.jadeleaf.com/v1`
* **Staging**: `https://api-staging.jadeleaf.com/v1`

---

## Authentication & Authorization
All endpoints except registration and menu fetching require a Bearer token:
```http
Authorization: Bearer <JWT_TOKEN_HERE>
```

### 1. User Registration
* **Endpoint**: `POST /auth/register`
* **Request Payload**:
  ```json
  {
    "username": "yixuan_tea",
    "email": "yixuan@jadeleaf.com",
    "password": "SecurePassword123",
    "phoneNumber": "+8613800138000"
  }
  ```
* **Success Response (`201 Created`)**:
  ```json
  {
    "id": 481,
    "username": "yixuan_tea",
    "email": "yixuan@jadeleaf.com",
    "role": "CUSTOMER"
  }
  ```

### 2. User Login
* **Endpoint**: `POST /auth/login`
* **Request Payload**:
  ```json
  {
    "email": "yixuan@jadeleaf.com",
    "password": "SecurePassword123"
  }
  ```
* **Success Response (`200 OK`)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 481,
      "username": "yixuan_tea",
      "email": "yixuan@jadeleaf.com",
      "role": "CUSTOMER"
    }
  }
  ```

---

## Digital Menu & Catalog

### 1. Fetch All Products (Bilingual, Cached)
* **Endpoint**: `GET /products`
* **Headers**: `Accept-Language: zh-CN` (or `en-US` to automatically adjust content hierarchies)
* **Success Response (`200 OK`)**:
  ```json
  [
    {
      "id": 101,
      "nameCn": "西湖龙井 (特级)",
      "nameEn": "West Lake Longjing (Imperial)",
      "category": "Premium Loose Leaf Teas",
      "price": 128.00,
      "origin": "West Lake, Hangzhou, Zhejiang, China",
      "brewingTemp": "80°C - 85°C",
      "tastingNotes": "Vibrant, fresh and nutty with orchid-like sweetness.",
      "rating": 4.9,
      "isFeatured": true
    }
  ]
  ```

### 2. List New Product (Admin Only)
* **Endpoint**: `POST /admin/products`
* **Success Response (`201 Created`)**

---

## Table & Private Tea Room Reservations

### 1. Create Seating Booking
* **Endpoint**: `POST /reservations`
* **Request Payload**:
  ```json
  {
    "storeId": 1,
    "roomType": "Private Tatami Tea Room",
    "dateTime": "2026-07-15T14:00:00Z",
    "seatingPreference": "Window view",
    "pax": 4,
    "notes": "Traditional tea master requested for Gaiwan brewing showcase"
  }
  ```
* **Success Response (`201 Created`)**:
  ```json
  {
    "id": 302,
    "storeId": 1,
    "status": "CONFIRMED",
    "pointsAwarded": 100
  }
  ```

---

## Café Ordering & Customisation

### 1. Submit Order Ahead / Dine-In Checkout
* **Endpoint**: `POST /orders`
* **Request Payload**:
  ```json
  {
    "storeId": 1,
    "orderType": "ORDER_AHEAD",
    "totalPrice": 166.00,
    "customisationNotes": "Standard",
    "items": [
      {
        "productId": 101,
        "quantity": 1,
        "teaStrength": "Strong",
        "sugarLevel": "50%",
        "iceLevel": "No Ice",
        "milkOption": "Oat Milk"
      }
    ]
  }
  ```
* **Success Response (`210 Created`)**:
  ```json
  {
    "orderId": 8092,
    "orderNumber": "JL872391",
    "status": "RECEIVED",
    "pointsEarned": 166,
    "estimatedMinutes": 12
  }
  ```

### 2. Fetch Active Order Status (Live Polling or WebSocket)
* **Endpoint**: `GET /orders/{orderId}/status`
* **Success Response (`200 OK`)**:
  ```json
  {
    "orderId": 8092,
    "orderNumber": "JL872391",
    "status": "PREPARING",
    "statusUpdatedTime": "2026-07-08T11:20:00Z"
  }
  ```

---

## Club Membership & Loyalty Rewards

### 1. Fetch Membership Balance & QR Card Status
* **Endpoint**: `GET /membership/me`
* **Success Response (`200 OK`)**:
  ```json
  {
    "userId": 481,
    "membershipNumber": "JL-M-980123",
    "points": 1250,
    "tier": "Gold",
    "cardBalance": 880.00
  }
  ```

### 2. Digital Card Recharge
* **Endpoint**: `POST /membership/recharge`
* **Request Payload**:
  ```json
  {
    "amount": 200.00,
    "paymentToken": "tok_visa_87239"
  }
  ```
* **Success Response (`200 OK`)**:
  ```json
  {
    "newBalance": 1080.00,
    "pointsEarned": 200
  }
  ```
