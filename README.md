# 📝 Blog API

A fully-featured RESTful Blog API built with **Spring Boot** and **Spring Security**, 
secured with JWT authentication and packed with features like email verification, 
password reset, post/comment management, likes, and file uploads.

---

## 🚀 Features

- 🔐 JWT-based Authentication & Authorization
- 📧 User Registration with Email Verification
- 🔑 Password Reset via Email
- 📝 Blog Post Management (CRUD)
- 💬 Comment Management
- ❤️ Post Likes
- 🖼️ File / Image Uploads
- 📄 Pagination & Sorting
- ⚠️ Global Exception Handling
- 📖 API Documentation via Swagger / OpenAPI

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Spring Boot | Application Framework |
| Spring Security | Authentication & Authorization |
| JWT | Token-based Security |
| Spring Data JPA / Hibernate | ORM & Database Access |
| MySQL | Database |
| Maven | Build Tool |
| Swagger / OpenAPI | API Documentation |
| JavaMailSender | Email Service |
| Thymeleaf | Email Templates |

---

## 📁 Project Structure
```
src/
├── config/          # Security, OpenAPI configuration
├── controller/      # REST Controllers
├── entity/          # JPA Entities
├── event/           # Application Events
├── exception/       # Global Exception Handling
├── listener/        # Event Listeners
├── payload/         # DTOs & Request/Response classes
├── repository/      # JPA Repositories
├── security/        # JWT, UserDetails, Filters
└── service/         # Business Logic
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- MySQL
- Maven

### 1. Clone the Repository
```bash
git clone https://github.com/Meraj260901/Blog-API.git
cd Blog-API
```

### 2. Create a MySQL Database
```sql
CREATE DATABASE blogdb;
```

### 3. Configure application.properties
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/blogdb
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JWT
jwt.secret=YOUR_JWT_SECRET
jwt.expiration=86400000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_EMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

### 5. Access Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📬 API Endpoints

### 🔐 Auth
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register a new user | ❌ |
| POST | `/api/auth/login` | Login and get JWT token | ❌ |
| GET | `/api/auth/verify-email` | Verify email address | ❌ |
| POST | `/api/auth/forgot-password` | Request password reset | ❌ |
| POST | `/api/auth/reset-password` | Reset password | ❌ |

### 📝 Posts
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/posts` | Get all posts (paginated) | ❌ |
| GET | `/api/posts/{id}` | Get post by ID | ❌ |
| POST | `/api/posts` | Create a new post | ✅ |
| PUT | `/api/posts/{id}` | Update a post | ✅ |
| DELETE | `/api/posts/{id}` | Delete a post | ✅ |

### 💬 Comments
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/posts/{postId}/comments` | Get all comments for a post | ❌ |
| POST | `/api/posts/{postId}/comments` | Add a comment | ✅ |
| DELETE | `/api/posts/{postId}/comments/{id}` | Delete a comment | ✅ |

### ❤️ Likes
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/posts/{postId}/likes` | Like a post | ✅ |
| DELETE | `/api/posts/{postId}/likes` | Unlike a post | ✅ |

### 👤 Users
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/users/{id}` | Get user by ID | ✅ |
| PUT | `/api/users/{id}` | Update user profile | ✅ |
| DELETE | `/api/users/{id}` | Delete user | ✅ |

---

## 🔒 Security

- All protected endpoints require a **JWT Bearer Token** in the request header:
```
Authorization: Bearer <your_token>
```
- Tokens are generated upon successful login
- Email verification is required before login
- Passwords are encrypted using **BCrypt**

---

## 📧 Email Features

- **Verification Email** — Sent after registration with a verification link
- **Password Reset Email** — Sent with a secure reset link when requested

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 🙋‍♂️ Author

**Meraj**
- GitHub: [@Meraj260901](https://github.com/Meraj260901)
- LinkedIn: [Your LinkedIn Profile](https://linkedin.com/in/YOUR_PROFILE)

---

⭐ If you found this project helpful, please give it a star!
