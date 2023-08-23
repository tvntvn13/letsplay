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

#### **Public**

- _**GET**_ | get all products
- _https://localhost:443/api/products_
- **RESPONSE**:

```JSON
"detail": "OK",
"payload": [
  {
    "name": "best product",
    "description": "this is awesome product",
    "price": 420.99
  },
  {
    "name": "bad product",
    "description": "this is not a great product",
    "price": 12.49
  }
],
"status": 200
```

---

#### **Authentication**

- _**POST**_ | sign up
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
(only alphanumerical with reasonable lengths_ **[[1]](#reference)** )

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

- **_POST_** | login
- _https://localhost:443/api/auth/login_
- request body:

```JSON
{
  "name": "john",
  "password": "sEcRe7pA5sW0rd"
}
```

**required fields:** _name **string**, password **string** ( only alphanumerical
with reasonable lengths_ **[1]: (#reference)** )

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

##### [[2]note](#reference2)

---

### **Registered user**

---

### **USERS**

---

- **_GET_** | get all the users
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

- **_GET_** | get user by name
- _https://localhost:443/api/users?name=name_
- **required parameter:** _name **string** (only alphanumerical with reasonable
  lengths_ **[[1]](#reference)**)
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

- **_GET_** | get your own info
- _https://localhost:443/api/users/whoami_
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

---

- **_PUT_** | update user
- _https://localhost:443/api/users_
- request body: _(at least one field is required)_

```JSON
{
  "name": "newname",
  "email": "bob@newname.com",
  "password": "newpassword"
}
```

```JSON
{
  "name": "newname",
  "email": "bob@newname.com"
}
```

```JSON
{
  "name": "newname"
}
```

**required fields:** _name **string**, email **string**, password **string** (
only alphanumerical with reasonable lengths_ **[[1]](#reference)** )

- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": {
    "email": "bob@newname.com",
    "name": "newname",
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
  "detail": "Not Acceptable",
  "payload": "email invalid: bob@newname",
  "status": 406
}
```

---

### **PRODUCTS**

---

- **_POST_** | create a new product
- _https://localhost:443/api/products_
- request body:

```JSON
{
  "name": "Vaccuum cleaner PX3000",
  "description": "the best vaccuum cleaner",
  "price": 49.00
}
```

```JSON
{
  "name": "Vaccuum cleaner PX3000",
  "description": "the best vaccuum cleaner",
  "price": 49.00
}
```

**required fields:** _name **string**, description **string**, price **double**
( only alphanumerical with reasonable lengths_ **[[1]](#reference)** )

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

- **_GET_** | get product by name
- _https://localhost:443/api/products?name=name_
- **required parameter:** _name **string** (only alphanumerical with reasonable
  lengths_ **[[1]](#reference)**)
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": {
    "description": "product description",
    "name": "productname",
    "price": 14.99
  },
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Access Denied",
  "payload": "An error occurred",
  "status": 403
}
```

```JSON
{
  "detail": "Not Found",
  "payload": "product <productname> not found",
  "status": 404
}
```

---

- **_GET_** | get product by owner
- _https://localhost:443/api/products?owner=owner_
- **required parameter:** _owner **string** (only alphanumerical with reasonable
  lengths_ **[[1]](#reference)**)
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": [
    {
      "description": "product description",
      "name": "productname",
      "price": 14.99
    },
    {
      "description": "2nd description",
      "name": "2nd name",
      "price": 3.50
    }
  ],
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Access Denied",
  "payload": "An error occurred",
  "status": 403
}
```

```JSON
{
  "detail": "Not Found",
  "payload": "no products found for user: <name>",
  "status": 404
}
```

---

- **_GET_** | get all own products
- _https://localhost:443/api/products/myproducts_
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": [
    {
      "description": "product description",
      "name": "productname",
      "price": 14.99
    },
    {
      "description": "2nd description",
      "name": "2nd name",
      "price": 3.50
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
  "path": "/api/products/myproducts",
  "status": 403,
  "timestamp": "2023-08-23T14:38:53.241+00:00"
}
```

---

- **_PUT_** | update product
- _https://localhost:443/api/products?name=name_
- **required parameter:** _name **string** (only alphanumerical with reasonable
  lengths_ **[[1]](#reference)**)
- request body: _(at least one field is required)_

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
**[[1]](#reference)** )

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

- **_DELETE_** | delete product
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
  "payload": "no rights for deleting product <productname>",
  "status": 403
}
```

---

### ADMIN

---

- **_DELETE_** | delete user and all users products
- _https://localhost:443/api/users?name=name_
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": "user and all products deleted by name: john",
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Not Found",
  "payload": "user not found by name: john",
  "status": 404
}
```

```JSON
{
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users",
  "status": 403,
  "timestamp": "2023-08-23T14:56:35.240+00:00"
}
```

---

- **_POST_** | grant admin rights to user
- _https://localhost:443/api/users/admin?name=name_
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": "admin rights granted to user: john",
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Not Found",
  "payload": "user not found by name: john",
  "status": 404
}
```

```JSON
{
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users/admin",
  "status": 403,
  "timestamp": "2023-08-23T14:56:35.240+00:00"
}
```

---

- **_DELETE_** | remove admin rights from user
- _https://localhost:443/api/users/admin?name=name_
- **RESPONSE**:
- success:

```JSON
{
  "detail": "OK",
  "payload": "admin rights removed from user: john",
  "status": 200
}
```

- error:

```JSON
{
  "detail": "Not Found",
  "payload": "user not found by name: john",
  "status": 404
}
```

```JSON
{
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/users/admin",
  "status": 403,
  "timestamp": "2023-08-23T14:56:35.240+00:00"
}
```

- if you try to remove admin rights from default admin user, it will delete
  current user and all its products.
- if you're loggeed in as default admin, and try to remove rights from yourself,
  you'd be surprised.

---

<a id="reference"></a>

##### notes:

---

###### [1]: reasonable lenghts are:

##### _USERS_:

- password: 4 - 50
- name: 3 - 30
- email: valid

##### _PRODUCTS_:

- name: 3 - 60
- description: 3 - 70
- price: 0 - 1.79769313486231570e+308d

<a id="reference2"></a>

###### [2]: notice

#### _JWT-Authentication:_

- after you login, you will receive a token. This will have to be attached to
  the header of each subsequent request, using the "Authorization: Bearer"
  pattern.

#### _SSL-verification:_

- due to the certificate beign self signed, you need to disable ssl-verification
  on your API-client to make requests to the API.
