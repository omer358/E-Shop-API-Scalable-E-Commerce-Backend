# **🛒 E-Shop API – Spring Boot Backend for E-Commerce**

## **📌 Overview**  
**E-Shop API** is a full-featured, scalable **Spring Boot backend** powering an e-commerce application. It handles user authentication, product management, cart operations, and secure order processing using **clean architecture, robust security practices**, and **modular design principles**. Built for real-world deployment with scalability, maintainability, and developer ergonomics in mind.

---

## **🚀 Features**
- ✅ **JWT-Based Authentication & Role-Based Access Control (RBAC)**
- ✅ **Cart & Order Management** – Add, update, remove, and checkout
- ✅ **Product & Category Management** – CRUD operations with stock handling
- ✅ **Image Upload & Storage**
- ✅ **Payment Gateway Integration** – Seamlessly integrated with **Moyasar**
- ✅ **Custom Exception Handling** – Global handlers for graceful error responses
- ✅ **DTO Mapping with MapStruct**
- ✅ **Swagger/OpenAPI** Documentation
- ✅ **Modular Service Layer with Interface Segregation**
- ✅ **CI-Ready** – Easily pluggable into GitHub Actions / Jenkins pipelines
- ✅ **Database Agnostic** – Supports PostgreSQL or MySQL with JPA abstraction

---

## **🛠️ Tech Stack**
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security + JWT**
- **Spring Data JPA**
- **PostgreSQL / MySQL**
- **Moyasar Payment API**
- **Lombok**
- **Docker**
- **JUnit & Mockito**
- **Swagger (OpenAPI 3)**

---

## **📂 Project Structure**
Following **clean and layered architecture** principles:
```
.
└── shop
    ├── cart
    ├── category
    ├── common
    ├── exceptions
    ├── image
    ├── order
    ├── product
    ├── ShopApplication.java
    └── user

```

---

## **⚙️ Setup & Installation**

### **1️⃣ Clone the Repo**
```bash
git clone https://github.com/omer358/cart-shop.git
cd cart-shop
```

### **2️⃣ Configure the Database**
Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/eshop_db
    username: your_db_user
    password: your_db_password
```

### **3️⃣ Run the App**
With **Maven**:
```bash
mvn clean install
mvn spring-boot:run
```
<!--
Or with **Docker**:
```bash
docker-compose up --build
```
-->
---

## **📡 API Endpoints (Sample)**

| Method   | Endpoint                      | Description                 | Auth? |
|----------|-------------------------------|-----------------------------|-------|
| `POST`   | `/api/cart/add`               | Add item to cart            | ✅    |
| `GET`    | `/api/cart`                   | View user cart              | ✅    |
| `POST`   | `/api/cart/checkout`          | Checkout current cart       | ✅    |
| `POST`   | `/api/products`               | Add new product             | ✅ (Admin) |
| `GET`    | `/api/products`               | List all products           | ❌    |
| `POST`   | `/api/auth/register`          | Register new user           | ❌    |
| `POST`   | `/api/auth/login`             | Login & get JWT             | ❌    |

📘 See full API documentation in **Swagger UI**:  
👉 http://localhost:8080/swagger-ui/index.html  

---

## **🛡️ Authentication & Security**
- **JWT Bearer Tokens** for stateless auth
- Supports **user roles** (Admin, Customer)
- Secure headers & endpoint protection via **Spring Security**

Sample request header:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
```

---
<!--
## **💳 Payment Integration**
🔌 Integrated with **Moyasar API** to handle card payments.  
- Secured payment flow using tokenization  
- Supports order reference tracking  
- Can be extended to support Stripe, PayPal, etc.
-->
---

## **✅ Testing**
Run unit and service-level tests:
```bash
mvn test
```
Frameworks used:
- **JUnit 5**
- **Mockito**
- **AssertJ**

---
<!-- 
## **📦 Docker Support**
Use `Dockerfile` and `docker-compose.yml` for seamless deployment:
```bash
docker-compose up --build
```
-->

---

## **🌍 Deployment Ready**
The app is prepped for deployment with:
- Environment-specific configs  
- CI/CD friendly structure (supports GitHub Actions, Jenkins)  
- Production-grade logging and error responses

---

## **🤝 Contributing**
Want to improve this?  
1. Fork the repo  
2. Create your feature branch (`git checkout -b feature/YourFeature`)  
3. Commit your changes  
4. Push and open a PR 🚀

---

## **📜 License**
Licensed under **Apache 2.0** – free to use, modify, distribute.

---

## **📞 Contact**
👤 **Omar Elkhalifa**  
📧 Email: omermaki358@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/omer-maki)  
🐙 [GitHub](https://github.com/omer358)

---
