[🇩🇪 Auf Deutsch wechseln](README_de.md)

# 📚 VOEBB Final Project – Library Management System

A **monolithic Spring Boot** application for managing a library catalog (OPAC – Open Public Access Catalog), designed for both **library users** and **personal working at the library**.

---

## 👥 Team

This project was developed by:

* [Natalie Lazarev](https://github.com/nat-laz)
* [Mitali Soti](https://github.com/mitalisoti)
* [Alex Bruch](https://github.com/bruch-alex)
* [Marc Stiehm](https://github.com/Rikupp17)

---

## ✨ Overview

This project simulates a full-featured **library system** covering all key functionalities needed by a modern public library. The system allows users to **browse, borrow, and manage books**, while also enabling admins to **maintain the catalog, track and manage borrows and reservations, and manage users**.

---

## 🎯 Features

### 👤 User-Facing

* 📖 Browse library catalog
* ✅ Create user accounts
* 📚 Borrow books
* ⏰ View and manage overdue books
* 🧾 See personal borrowing history

### 🛠 Admin Panel

* ➕ Add, update, or delete books
* 👥 Manage library users
* 📊 Overview of all borrowings
* ❌ Handle overdue returns
* 📚 and much more...

---

## 🧱 Tech Stack

| Layer          | Technology                         |
| -------------- | ---------------------------------- |
| **Backend**    | Java 17, Spring Boot               |
| **Frontend**   | Thymeleaf, Bootstrap 5, JavaScript |
| **Database**   | PostgreSQL                         |
| **Build Tool** | Maven                              |
| **Testing**    | JUnit                              |

> Extensive use of custom JavaScript to enhance Bootstrap components beyond defaults.

---

## 📁 Project Structure

```bash
voebb-final-project/
├── src/
│   ├── main/
│   │   ├── java/com/voebb/
│   │   │   ├── config/                 # Security and application configuration
│   │   │   ├── controller/             # Web controllers (User, Admin, Auth)
│   │   │   ├── model/                  # JPA entities (User, Book, Borrow, etc.)
│   │   │   ├── repository/             # Spring Data repositories
│   │   │   ├── service/                # Business logic and service layer
│   │   │   ├── dto/                    # Data Transfer Objects for forms and views
│   │   │   ├── exceptions/             # Custom exception classes and handlers
│   │   │   └── job/                    # Scheduled tasks (e.g. cron jobs for overdue handling)
│   │   ├── resources/
│   │   │   ├── templates/              # Thymeleaf templates
│   │   │   ├── static/                 # JS, CSS, images
│   │   │   └── application.properties  # Spring Boot config
│   └── test/                           # Unit and integration tests
├── pom.xml                             # Maven dependencies and build config
└── README.md                           # Project description
```

---

## 🔐 Security & Access

* No public API endpoints
* Internal endpoints for admin access (secured)
* Admin actions are protected by role-based access control

---

## 🧭 Development Notes

* Application follows a monolithic structure for simplicity and cohesion.
* Cron jobs are used to regularly check for overdue books and send notifications.
* Custom exceptions improve error handling across both admin and user views.
* Internal endpoints are secured and role-restricted via Spring Security.
* Bootstrap was extended with custom JavaScript to enhance UI interactivity.
