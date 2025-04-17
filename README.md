# 🛒 E-Shop API – Spring Boot Backend for E-Commerce

## 📌 Overview
**E-Shop API** is a fully modular and secure **Spring Boot** backend application built to support an e-commerce platform. It includes user authentication, product catalog, order placement, cart operations, image uploads, and address management. Built using clean architecture and battle-tested design principles, it's ready for production use and scaling.

---

## 🚀 Features

- ✅ **JWT Authentication & Role-Based Authorization**
- ✅ **Product & Category Management**
- ✅ **User Profile & Address Management**
- ✅ **Cart & Order Handling**
- ✅ **Image Upload via Multipart**
- ✅ **Secure REST API with Global Exception Handling**
- ✅ **OpenAPI (Swagger) Documentation**
- ✅ **DTO Mapping with ModelMapper**
- ✅ **Layered Architecture (Controller, Service, Repository, Mapper)**
- ✅ **Unit & Integration Tests**

---

## 🧠 Tech Stack

| Layer        | Technology                      |
|--------------|----------------------------------|
| Language     | Java 17                         |
| Framework    | Spring Boot 3.x                 |
| Security     | Spring Security + JWT           |
| Database     | PostgreSQL / MySQL              |
| ORM          | Spring Data JPA                 |
| Docs         | springdoc-openapi (Swagger)     |
| Testing      | JUnit 5, Mockito                |
| Dev Tools    | Lombok, MapStruct               |

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/omo/shop/
│   │   ├── auth/           # Login & JWT
│   │   ├── address/        # User shipping address
│   │   ├── cart/           # Cart + cart item
│   │   ├── category/       # Product categories
│   │   ├── common/         # Constants, responses, exceptions
│   │   ├── image/          # Product image uploads
│   │   ├── order/          # Orders & order items
│   │   ├── product/        # Product catalog
│   │   ├── security/       # JWT filters & config
│   │   ├── user/           # User data, profile, roles
│   └── resources/
│       └── application.yml
```

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/omer358/cart-shop.git
cd cart-shop
```

### 2️⃣ Configure Your DB
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/eshop_db
    username: your_db_user
    password: your_db_password
```

### 3️⃣ Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🔐 Authentication

- Auth is handled using **JWT Tokens**
- Roles supported: `ROLE_USER`, `ROLE_ADMIN`
- Token must be provided in the `Authorization` header:
  ```http
  Authorization: Bearer <your-token>
  ```

---

## 📡 Sample API Endpoints

| Method | Endpoint                     | Description                 | Auth Required |
|--------|------------------------------|-----------------------------|---------------|
| POST   | `/api/auth/register`         | Register new user           | ❌            |
| POST   | `/api/auth/login`            | Login and receive JWT       | ❌            |
| GET    | `/api/products`              | Get all products            | ❌            |
| POST   | `/api/products/add`          | Add a product (admin only)  | ✅            |
| GET    | `/api/cart`                  | Get current user's cart     | ✅            |
| POST   | `/api/cart/add`              | Add item to cart            | ✅            |
| POST   | `/api/orders/place-order`    | Place a new order           | ✅            |
| GET    | `/api/addresses`             | Get user's addresses        | ✅            |

📘 **View full API docs** via Swagger UI:  
🔗 http://localhost:8080/swagger-ui/index.html

---

## 📄 Exception Handling

Handled globally via `@ControllerAdvice`.  
Standardized response:
```json
{
  "message": "Product not found",
  "status": 404
}
```

---

## 🧪 Testing

Run tests with:
```bash
mvn test
```

Includes:
- ✅ Unit tests for services
- ✅ Controller tests with MockMvc
- ✅ Mapper tests

---

## 🔓 Security

- JWT filter via `OncePerRequestFilter`
- Passwords hashed with `PasswordEncoder`
- Access controlled by roles using method-level `@PreAuthorize`

---

## 📈 CI/CD Ready

- Clean architecture supports **modular builds**
- Easily integrated into pipelines like GitHub Actions, GitLab, Jenkins
- Environment-specific configurations for prod-ready deployment

---

## 🤝 Contributing

Contributions are welcome!  
Please fork the repo and open a pull request.

---

## 📜 License

Licensed under the **Apache 2.0 License**.  
Free to use, modify, and distribute.

---

## 👨‍💻 Author

**Omar Elkhalifa**  
📧 omermaki358@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/omer-maki)  
🐙 [GitHub](https://github.com/omer358)

---

> 🚧 _Payment integration is under development and coming soon..._
