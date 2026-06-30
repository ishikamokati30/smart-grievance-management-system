# 🚀 ServiceSync – Smart Grievance Management System

ServiceSync is a full-stack Smart Grievance Management System that enables citizens to report civic issues while providing administrators with a centralized platform to manage, assign, and resolve complaints efficiently.

The system includes secure authentication, role-based access control, department management, complaint tracking, email notifications, and an intuitive dashboard for both users and administrators.

---

## 🌐 Live Demo
https://servicesync-six.vercel.app/

### Backend API
https://servicesync-backend-76h0.onrender.com

### Swagger API Documentation
https://servicesync-backend-76h0.onrender.com/swagger-ui/index.html

---

# 📌 Features

## 👤 User Features

- User Registration
- Secure Login using JWT Authentication
- File New Complaints
- Upload Complaint Details
- View Complaint History
- Track Complaint Status
- Profile Management
- Email Notifications
- Responsive Dashboard

---

## 👨‍💼 Admin Features

- Admin Login
- Dashboard Analytics
- Manage Departments
- View All Users
- View All Complaints
- Update Complaint Status
- Assign Departments
- Change User Roles
- Delete Departments
- Monitor Complaint Resolution

---

# 📊 Complaint Workflow

Citizen
↓

Register/Login
↓

Submit Complaint
↓

Complaint Assigned to Department
↓

Admin Reviews Complaint
↓

Status Updated
↓

Citizen Receives Email Notification
↓

Complaint Resolved

---

# 🛠️ Tech Stack

## Frontend

- HTML5
- CSS3
- JavaScript (ES6)

---

## Backend

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- Maven

---

## Database

- PostgreSQL
- Neon Database

---

## Deployment

### Frontend

- Vercel

### Backend

- Render (Docker)

### Database

- Neon PostgreSQL

---

## Documentation

- Swagger OpenAPI

---

# 📂 Project Structure

```
smart-grievance-management-system/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── mvnw
│   └── ...
│
├── frontend/
│   ├── css/
│   ├── js/
│   ├── assets/
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── dashboard.html
│   └── ...
│
└── README.md
```

---

# 🔐 Authentication

The application uses JWT (JSON Web Token) authentication.

### Roles

- USER
- ADMIN

Role-based authorization ensures secure access to protected endpoints.

---

## Email Notification

Automatic email notifications are sent when:

- Complaint status changes
- Complaint is resolved

---

# 📸 Screenshots

## Home Page
<img width="400" height="400" alt="image" src="https://github.com/user-attachments/assets/81daca33-29fd-4fb4-a250-64ac15c44ebd" />

---

## User Dashboard
<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/abc682aa-e606-4bb5-b709-476abcd1750d" />

---

## Admin Dashboard
<img width="400" height="400" alt="image" src="https://github.com/user-attachments/assets/3583e4b9-db13-42a4-81dd-62be5d9d9eb4" />

---

## Complaint Form
<img width="300" height="300" alt="image" src="https://github.com/user-attachments/assets/86a2f839-8753-415c-a239-811b341de338" />

---

# 🚀 Getting Started

## Clone Repository

```bash
git clone https://github.com/ishikamokati30/smart-grievance-management-system.git
```

---

## Backend Setup

```bash
cd backend
```

Run

```bash
./mvnw spring-boot:run
```

or on Windows

```bash
mvnw.cmd spring-boot:run
```

---

## Frontend Setup

```bash
cd frontend
```

Run a local server

Python

```bash
python -m http.server 5500
```

Open

```
http://localhost:5500
```

# 🎯 Future Improvements

- Google Maps Integration
- Complaint Priority Prediction using AI
- Multi-language Support

---

