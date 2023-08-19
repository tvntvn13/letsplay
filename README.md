# **LETSPLAY**

### Introduction

- Letsplay is an **RESTful CRUD API** for managing **USERS** and **PRODUCTS**.
  The project is part of _Gritlab's_ Java curriculum. The project is written in
  Java, and it uses **Spring Boot** and **MongoDB** to manage the connections,
  database and security.

---

### Installation

- **Pre-requisites:**
  - MongoDB
  - Spring
  - Java
  - HTTPie, Postman etc. API-tool
  - Knowledge about HTTP-requests
  ---
- Clone the repo.
- Go to the root of the project
- Start MongoDB:
  ```bash
  mongod --dbpath /usr/local/var/mongodb --logpath /usr/local/var/log/mongodb/mongo.log --fork
  ```
- Start Spring:
  ```bash
  mvn spring-boot:run
  ```
- server will start at:
  - _https://localhost:443_

---

### API-Endpoints

---

#### Public

- _**GET**_ Products
  - GET request to _https://localhost:443/api/products_
    - example with HTTPie:
    ```bash
    https get :443/api/products
    ```
    - **RESPONSE**:
    ```JSON
    [
      {
        "name": "best product",
        "description": "this is awesome product",
        "price": "420.99"
      },
      {
        "name": "bad product",
        "description": "this is not a great product",
        "price": "12.49"
      }
    ]
    ```

---

#### Authentication

- _**POST**_ Signup
  - Post request to _https://localhost:443/api/auth/signup_
    - request body:
    ```JSON
    {
      "name": "john",
      "email": "john@johnmail.com",
      "password": "sEcRe7pA5sW0rd"
    }
    ```
    - example with HTTPie:
    ```bash
    https post :443/api/auth/signup \
    name=john \
    email=john@johnmail.com \
    password=sEcRe7pA5sW0rd
    ```
    **required fields:** _name, email, password ( only alphanumerical with
    reasonable lengths, **[3 - 30]** )_
    - **RESPONSE**:
      - success:
        ```bash
        "user: john registered succesfully"
        ```
      - error:
        ```JSON
        {
          "detail": "Invalid request content.",
          "instance": "/api/auth/signup",
          "status": 400,
          "title": "Bad Request",
          "type": "about:blank"
        }
        ```
- **_POST_** login
  - Post request to _https://localhost:443/api/auth/login
    - request body:
    ```JSON
    {
      "name": "john",
      "password": "sEcRe7pA5sW0rd"
    }
    ```
    - example with HTTPie:
    ```bash
    https post :443/api/auth/login \
    name=john \
    password=sEcRe7pA5sW0rd
    ```
    **required fields:** _name, password ( only alphanumerical with reasonable
    lengths, **[3 - 30]** )_
    - **RESPONSE**:
      - success:
        ```JSON
        {
          "payload": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiaWF0IjoxNjkyNDQzMzk3LCJleHAiOjE2OTI0NjEzOTd9.pLQeWfe09NCHiGoN1hYxEMaVHNQxhZcIQ7jBLrq3S9g",
          "status": "succesfully logged in"
        }
        ```
        - error:
        ```JSON
        {
          "detail": "Failed to read request",
          "instance": "/api/auth/login",
          "status": 400,
          "title": "Bad Request",
          "type": "about:blank"
        }
        ```

---

#### Registered user

- **_POST_** Product
  - Post request to _https://localhost:443/api/products_
    - request body:
    ```JSON
    {
      "name": "Vaccuum cleaner PX3000",
      "description": "the best vaccuum cleaner",
      "price": 49.00
    }
    ```
    - example with HTTPie:
    ```bash
    https post :443/api/products \
    name='Vacuum cleaner PX3000' \
    description='the best vaccuum cleaner' \
    price=49
    ```
    **required fields:** _name **string**, description **string**, price
    **double** ( only alphanumerical with reasonable lengths, **[3 - 30]** )_
    - **RESPONSE**:
      - success:
      ```JSON
      {
        "description": "the best vaccuum cleaner",
        "name": "Vacuum cleaner PX3000",
        "price": 49.0
      }
      ```
      - error:
      ```JSON
      {
        "detail": "Failed to read request",
        "instance": "/api/auth/login",
        "status": 400,
        "title": "Bad Request",
        "type": "about:blank"
      }
      ```
      ```bash
      name is already taken Vacuum cleaner PX3000
      ```

---
