# 📝 CritiqueHub - Blogging Platform (Spring Boot)

CritiqueHub is a modern backend API for a **blogging platform**, built with **Spring Boot (Java)**, that supports:

- 🔐 **JWT Authentication** (Authors & Readers)
- 🏷️ CRUD for **Posts, Categories, Tags**
- 💬 **Comments & Likes** system
- 🔍 **Keyword Search**
- 📦 **Database migrations with Flyway**
- 🐳 **Dockerized deployment**
- 📄 **Swagger/OpenAPI documentation**

---

## 🚀 Tech Stack
- **Java 21**
- **Spring Boot 3.2**
- **Spring Security 6** + **JWT**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL 17**
- **Flyway** (DB migrations)
- **Maven**
- **Docker** (for containerized deployment)

---

## 📂 Project Structure

CritiqueHub
├── src/main/java/com/critiquehub
│ ├── config # Security, JWT, Swagger configs
│ ├── controller # REST Controllers
│ ├── dto # DTOs (request/response models)
│ ├── entity # JPA Entities
│ ├── repository # Spring Data Repositories
│ ├── service # Business Logic
│ └── CritiqueHubApplication.java
│
├── src/main/resources
│ ├── db/migration # Flyway SQL migrations
│ ├── application-dev.yml
│ └── application-prod.yml
│
├── pom.xml
└── README.md


---

## ⚙️ Setup & Run

### 1️⃣ Clone the Repository
git clone https://github.com/Nafi14078/CritiqueHub-Blogg.git
cd CritiqueHub


### 2️⃣ Configure Database
Create a PostgreSQL database and update credentials in:
spring:
datasource:
url: jdbc:postgresql://localhost:5432/postgres
username: postgres
password: yourpassword

## 🤝 Contributing
Pull requests are welcome! Follow standard fork → branch → PR workflow.
