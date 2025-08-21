Got it 👍 You want your README to explain how to **set up environment variables** properly (using `.env`) and **run the app with Docker Compose** (so people don’t have to manually copy long `docker run` commands).

Here’s an updated version of your README with a clean **Docker Compose workflow** and **`.env` file instructions**:

---

# **🛒 E-Shop API – Spring Boot Backend for E-Commerce**

## **📌 Overview**

**E-Shop API** is a full-featured, scalable **Spring Boot backend** powering an e-commerce application. It handles user authentication, product management, cart operations, and secure order processing using **clean architecture, robust security practices**, and **modular design principles**. Built for real-world deployment with scalability, maintainability, and developer ergonomics in mind.

---

## **🚀 Features**

* ✅ **JWT-Based Authentication & Role-Based Access Control (RBAC)**
* ✅ **Cart & Order Management** – Add, update, remove, and checkout
* ✅ **Product & Category Management** – CRUD operations with stock handling
* ✅ **Image Upload & Storage**
* ✅ **Custom Exception Handling** – Global handlers for graceful error responses
* ✅ **DTO Mapping with MapStruct**
* ✅ **Swagger/OpenAPI** Documentation
* ✅ **Modular Service Layer with Interface Segregation**
* ✅ **CI/CD Ready** – Automated pipeline with GitHub Actions & Docker
* ✅ **Database Agnostic** – Supports PostgreSQL or MySQL with JPA abstraction

---

## **🛠️ Tech Stack**

* **Java 21**
* **Spring Boot 3.x**
* **Spring Security + JWT**
* **Spring Data JPA**
* **PostgreSQL / MySQL**
* **Lombok**
* **Docker & GHCR (GitHub Container Registry)**
* **JUnit & Mockito**
* **Swagger (OpenAPI 3)**

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

---

### **2️⃣ Create a `.env` File**

In the project root, create a `.env` file and add your own values:

```env
# Database
MYSQL_ROOT_PASSWORD=1029384756
MYSQL_DATABASE=eshop_db
MYSQL_USER=eshop_user
MYSQL_PASSWORD=eshop_pass

# App
SPRING_DATASOURCE_URL=jdbc:mysql://shop-db:3306/eshop_db
SPRING_DATASOURCE_USERNAME=eshop_user
SPRING_DATASOURCE_PASSWORD=eshop_pass
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect

# Security
JWT_SECRET_KEY=your-secret-key-here
JWT_EXPIRATION=3600000
```

---

### **3️⃣ Run with Docker Compose**

Start both the **app** and **database** with one command:

```bash
  docker compose up -d
```

👉 This will start:

* `shop-db` → MySQL container
* `shop` → Your Spring Boot API (connected to MySQL automatically)

Check logs:

```bash
  docker compose logs -f shop
```

Stop containers:

```bash
  docker compose down
```

---

### **4️⃣ Access the Application**

* API → [http://localhost:9193](http://localhost:9193)
* Swagger UI → [http://localhost:9193/swagger-ui/index.html](http://localhost:9193/swagger-ui/index.html)

---

### **5️⃣ Example: Running without Compose (optional)**

If you only want the API (database must be running separately):

```bash
  docker run -d \
    --name shop \
    --network internal \
    --env-file .env \
    -p 9193:9193 \
    ghcr.io/omer358/e-shop-api-scalable-e-commerce-backend:latest
```

---

## **⚙️ CI/CD Pipeline**

This project includes a full **CI/CD pipeline using GitHub Actions**:

### **🔄 Workflow Stages**

1. **Test Job** 🧪 – Runs unit & integration tests with Maven.
2. **Build Job** 🏗 – Builds the Spring Boot JAR package (without tests).
3. **Docker Build & Push** 🐳 – Builds a Docker image and pushes it to **GitHub Container Registry (GHCR)**.

📦 Images are available at:
👉 `ghcr.io/omer358/e-shop-api-scalable-e-commerce-backend`

---

## **📡 API Endpoints (Sample)**

| Method | Endpoint             | Description           | Auth?     |
| ------ | -------------------- | --------------------- | --------- |
| `POST` | `/api/cart/add`      | Add item to cart      | ✅         |
| `GET`  | `/api/cart`          | View user cart        | ✅         |
| `POST` | `/api/cart/checkout` | Checkout current cart | ✅         |
| `POST` | `/api/products`      | Add new product       | ✅ (Admin) |
| `GET`  | `/api/products`      | List all products     | ❌         |
| `POST` | `/api/auth/register` | Register new user     | ❌         |
| `POST` | `/api/auth/login`    | Login & get JWT       | ❌         |

📘 Full API docs available in **Swagger UI**.

---

## **🛡️ Authentication & Security**

* **JWT Bearer Tokens** for stateless auth
* Supports **user roles** (Admin, Customer)
* Secure headers & endpoint protection via **Spring Security**

---

## **✅ Testing**

Run unit and service-level tests:

```bash
  ./mvnw test
```

Frameworks used:

* **JUnit 5**
* **Mockito**
* **AssertJ**

---

## **🌍 Deployment Ready**

The app is prepped for deployment with:

* Environment-specific configs
* Dockerized builds via GHCR
* CI/CD pipeline with GitHub Actions
* Production-grade logging and error handling

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
📧 Email: [omermaki358@gmail.com](mailto:omermaki358@gmail.com)
🔗 [LinkedIn](https://www.linkedin.com/in/omer-maki)
🐙 [GitHub](https://github.com/omer358)
