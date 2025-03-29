# **🛒 Cart Shop – Spring Boot E-Commerce Cart API**  

## **📌 Overview**  
This project is a **Spring Boot-based shopping cart API** that manages products, carts, and checkout functionalities. It demonstrates **clean architecture, RESTful principles, and best practices** in Java and Spring Boot.  

---

## **🚀 Features**  
✅ **Cart Management** – Add, remove, and update cart items.  
✅ **Product Management** – List products with prices and stock levels.  
✅ **Total Calculation** – Automatically updates the cart total amount.  
✅ **User Authentication** – Secure API endpoints (JWT authentication).  
✅ **Database Integration** – Uses PostgreSQL (or MySQL).  
✅ **Exception Handling** – Global exception handling with meaningful responses.  

---

## **🛠️ Tech Stack**  
- **Java 17**  
- **Spring Boot 3.x**  
- **Spring Data JPA**  
- **Spring Security (JWT)**  
- **PostgreSQL / MySQL**  
- **Lombok**  
- **MapStruct (DTO Mapping)**  
- **Docker (Optional)**  
- **JUnit & Mockito (Testing)**  

---

## **📂 Project Structure**  
```
📦 cart-shop
 ┣ 📂 src/main/java/com/omo/shop
 ┃ ┣ 📂 controllers      # REST Controllers
 ┃ ┣ 📂 services         # Business Logic Layer
 ┃ ┣ 📂 repositories     # Data Access Layer (Spring Data JPA)
 ┃ ┣ 📂 models           # Entity Classes
 ┃ ┣ 📂 dtos             # DTOs for API Responses
 ┃ ┣ 📂 exceptions       # Custom Exceptions & Handlers
 ┃ ┗ 📜 CartShopApplication.java # Main Entry Point
 ┣ 📜 README.md          # Project Documentation
 ┣ 📜 pom.xml            # Maven Dependencies
 ┗ 📜 application.yml    # Configuration
```

---

## **⚙️ Installation & Setup**  

### **1️⃣ Clone the Repository**  
```bash
git clone https://github.com/yourusername/cart-shop.git
cd cart-shop
```

### **2️⃣ Configure the Database**  
Edit `application.yml` to set up your **PostgreSQL/MySQL** database:  
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cartshop_db
    username: your_db_user
    password: your_db_password
```

### **3️⃣ Build & Run the Application**  
Using **Maven**:  
```bash
mvn clean install
mvn spring-boot:run
```

Using **Docker (Optional)**:  
```bash
docker-compose up --build
```

---

## **📝 API Endpoints**  
| Method | Endpoint               | Description             | Auth Required? |
|--------|------------------------|-------------------------|---------------|
| `POST` | `/api/cart/add`        | Add item to cart       | ✅ Yes |
| `DELETE` | `/api/cart/remove/{id}` | Remove item from cart | ✅ Yes |
| `GET` | `/api/cart`             | Get cart details       | ✅ Yes |
| `POST` | `/api/cart/checkout`   | Checkout cart          | ✅ Yes |

📌 **Full API Docs** → [Postman Collection](#) (Link to API collection)  

---

## **🛡️ Security & Authentication**  
- Uses **JWT Authentication** for secured endpoints.  
- Register/Login to get a JWT token and include it in the `Authorization` header.  

Example:  
```
Authorization: Bearer your-jwt-token
```

---

## **✅ Testing**  
Run **unit tests** using JUnit & Mockito:  
```bash
mvn test
```

---

## **🤝 Contributing**  
1. Fork the repo  
2. Create a new branch (`feature-name`)  
3. Commit and push changes  
4. Open a **Pull Request**  

---

## **📜 License**  
This project is licensed under the **Apache 2.0 License** – feel free to use and modify it.  

---

## **📞 Contact**  
👤 **Omar**  
📧 Email: omermaki358@gmail.com <br>
🔗 LinkedIn: [https://www.linkedin.com/in/omer-maki](#)  <br>
🚀 GitHub: [omer358](https://github.com/omer358)  <br>
