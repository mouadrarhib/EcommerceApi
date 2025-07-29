# 🛒 Ecommerce API – Spring Boot Backend

A complete RESTful backend for an E-commerce platform built with **Java Spring Boot**.  
This project includes user authentication, product management, cart functionality, order processing, wishlist, and review features — all with **secure JWT authentication**, **role-based access**, and **CI/CD pipelines**.

---

## 📦 Features

### ✅ User & Security
- JWT Authentication & Authorization
- Role-based access control (USER / ADMIN)
- User profile view & update

### 🛍️ Products & Categories
- Product CRUD with stock, price, image URL, and visibility
- Category management
- Search & filter products by keyword or category

### 🛒 Shopping Cart
- Add/update/remove products from cart
- View and clear cart
- Sync with product stock

### 📦 Orders
- Place orders from cart
- Track order status (PENDING, SHIPPED, etc.)
- Cancel orders

### ⭐ Reviews & Ratings
- Leave product reviews and star ratings
- Update/delete your reviews
- See average product ratings

### ❤️ Wishlist
- Add or remove products from a personal wishlist
- Retrieve wishlist contents

---

## 🛠️ Tech Stack

| Layer      | Tech Used                             |
|------------|----------------------------------------|
| Language   | Java 17                                |
| Framework  | Spring Boot, Spring Security           |
| Database   | PostgreSQL                             |
| Auth       | JWT                                    |
| DevOps     | Docker, GitHub Actions, Docker Compose |
| Tools      | Postman, Swagger, IntelliJ, GitKraken  |

---

## 🐳 Dockerized Setup

### 📁 Structure:
```
EcommerceAPI/
├── EcommerceAPI/           # Spring Boot App
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── .github/workflows/      # CI/CD pipeline
│   └── ci.yml
├── docker-compose.yml      # Docker services (Postgres, Maildev, Ecommerce-Api)
└── README.md
```

### 🔧 To Run:
```bash
# Build and start containers
docker-compose up --build
```

It will spin up:
- ✅ PostgreSQL database
- ✅ Maildev for local SMTP testing
- ✅ Spring Boot API container

---

## 🧪 Testing

- `mvn test` runs unit/integration tests (with JUnit & Mockito)
- Code coverage generated via JaCoCo
- CI/CD pipeline runs on every push via GitHub Actions

---

## 🔐 Environment Variables (Set in GitHub Secrets or .env or application-secrets.yml)
```
DB_USERNAME=username
DB_PASSWORD=password
DB_NAME=ecommercedb
SPRING_MAIL_HOST=mail-dev
SPRING_MAIL_PORT=1025
```

---

## 🧭 API Documentation

> Available via Swagger UI once app is running:
```
http://localhost:8088/swagger-ui/index.html
```

---

## 🚀 CI/CD Pipeline

Implemented via GitHub Actions:
- Run tests on each push
- Build and package the JAR
- Build and push Docker image
---

## 📬 Contact

**Mouad Rarhib**  
📍 Casablanca, Morocco  
📧 morarhib@gmail.com  
🔗 [GitHub](https://github.com/MouadRarhib) • [Portfolio](https://mouad-rarhib-portfolio-bwvs.vercel.app/) • [LinkedIn](https://linkedin.com/in/rarhibmouad)

---

## 🖼️ Screenshots

Here are some screenshots showcasing key parts of the development and deployment process:

### ✅ Spring Boot API running in the console
![✅ Spring Boot API running in the console](Screens/console.png)

### 🐳 Docker Desktop showing active containers
![🐳 Docker Desktop showing active containers](Screens/docker desktop.png)

### 📦 DockerHub with the pushed image
![📦 DockerHub with the pushed image](Screens/dockerhub.png)

### 🚀 GitHub Actions CI/CD pipeline execution
![🚀 GitHub Actions CI/CD pipeline execution](Screens/Pipeline.png)

### 📬 Postman request and response for product API
![📬 Postman request and response for product API](Screens/postman.png)

### 📘 Swagger API documentation interface
![📘 Swagger API documentation interface](Screens/swagger.png)

