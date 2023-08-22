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
- _https://localhost:443/api/products_
- **RESPONSE**:

```JSON
"detail": "OK",
"payload": [
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
],
"status": 200
```

---

#### Authentication

- _**POST**_ Signup
- _https://localhost:443/api/auth/signup_
- request body:

```JSON
{
  "name": "john",
  "email": "john@johnmail.com",
  "password": "sEcRe7pA5sW0rd"
}
```

**required fields:** _name **string**, email **string**, password **string**
(only alphanumerical with reasonable lengths_ **[1]** )

- **RESPONSE**:
- success:

```JSON
{
  "detail": "Created",
  "payload": "user john registered succesfully",
  "status": 201
}
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
- _https://localhost:443/api/auth/login_
- request body:

```JSON
{
  "name": "john",
  "password": "sEcRe7pA5sW0rd"
}
```

**required fields:** _name **string**, password **string** ( only alphanumerical
with reasonable lengths_ **[1]** )

- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": "eyJhbGciOiJIUzI1NiJ9 ... ",
  "status": 200
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

- **_GET_** Users | get all the users
- _https://localhost:443/api/users_
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": [
    {
      "email": "taneli@taneli.com",
      "name": "taneli",
      "role": "user"
    },
    {
      "email": "test@gmail.com",
      "name": "tester",
      "role": "user"
    },
    {
      "email": "obama@jmail.com",
      "name": "obama",
      "role": "user"
    }
  ],
  "status": 200
}
```

- error:

```JSON
{
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users",
  "status": 403,
  "timestamp": "2023-08-22T15:21:24.548+00:00"
}
```

- **_GET_** User | get user by name
- _https://localhost:443/api/users?name=name_
- **required parameter:** _name **string** (only alphanumerical with reasonable
  lengths_ **[1]**)
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": {
    "email": "taneli@taneli.com",
    "name": "taneli",
    "role": "user"
  },
  "status": 200
}
```

- error:

```JSON
{
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users",
  "status": 403,
  "timestamp": "2023-08-22T15:21:24.548+00:00"
}
```

```JSON
{
  "detail": "Not Found",
  "payload": "user not found by name: name",
  "status": 404
}
```

---

- **_POST_** Product | create a new product
- _https://localhost:443/api/products_
- request body:

```JSON
{
  "name": "Vaccuum cleaner PX3000",
  "description": "the best vaccuum cleaner",
  "price": 49.00
}
```

**required fields:** _name **string**, description **string**, price **double**
( only alphanumerical with reasonable lengths_ **[1]** )

- **RESPONSE**:
- success:

```JSON
{
  "detail": "Created",
  "payload": {
    "description": "the best vaccuum cleaner",
    "name": "Vacuum cleaner PX3000",
    "price": 49.0
  },
  "status": 201
}
```

- error:

```JSON
{
  "detail": "Invalid request content.",
  "instance": "/api/products",
  "status": 400,
  "title": "Bad Request",
  "type": "about:blank"
}
```

```JSON
{
  "detail": "Conflict",
  "payload": "name is already taken Vacuum cleaner PX3000",
  "status": 409
}
```

---

- **_PUT_** Product | update product
- _https://localhost:443/api/products?name=name_
- request body:

```JSON
{
  "name": "Vaccuum cleaner PX4000",
  "description": "not great vaccuum cleaner",
  "price": 19.99
}
```

```JSON
{
  "description": "not great vaccuum cleaner",
  "price": 12.49
}
```

```JSON
{
  "name": "new name"
}
```

**required fields: at least one of these:** _name **string**, description
**string**, price **double** ( only alphanumerical with reasonable lengths_
**[1]** )

- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": {
    "description": "not great vaccuum cleaner",
    "name": "Vacuum cleaner PX3000",
    "price": 12.49
  },
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Invalid request content.",
  "instance": "/api/products",
  "status": 400,
  "title": "Bad Request",
  "type": "about:blank"
}
```

```JSON
{
  "detail": "Forbidden",
  "payload": "you don't have rights to this product",
  "status": 403
}
```

---

- **_DELETE_** Product | delete product
- _https://localhost:443/api/products?name=name_
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": "product <productname> deleted",
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Bad request",
  "payload": "<productname> not found",
  "status": 400
}
```

```JSON
{
  "detail": "Forbidden",
  "payload": "no rights for deleting product productname",
  "status": 403
}
```

---
