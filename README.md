# 🏥 Hospital Management System — REST API

A production-structured, backend-only RESTful API for running a hospital, built with **Java 17, Spring Boot 3.3.4, Spring Security, JWT Authentication, Spring Data JPA, Hibernate, MySQL / H2, and OpenAPI 3.0 (Swagger UI)**.

This project covers the complete operational core of a modern hospital: secure role-based login, patient records, doctor appointments with double-booking protection, walk-in queues with daily tokens, emergency triage, pharmacy inventory with expiry checks, inpatient admissions with bed allocation, billing and payments, department messaging, dashboards and reports — all delivered through a clean layered architecture.

<br/>

![Java](https://img.shields.io/badge/Java-17%20LTS-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-6DB33F?logo=springsecurity)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?logo=jsonwebtokens)
![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?logo=mysql&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-85EA2D?logo=swagger&logoColor=black)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

---

## 📋 Table of Contents

- [What is this project?](#-what-is-this-project)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [How it Works — Architecture](#-how-it-works--architecture)
- [Project Structure](#-project-structure)
- [Domain Model](#-domain-model)
- [API Reference](#-api-reference)
- [How to Use the API — Request and Response Examples](#-how-to-use-the-api--request-and-response-examples)
- [Authentication — How JWT Works in this Project](#-authentication--how-jwt-works-in-this-project)
- [Business Rules and Validations](#-business-rules-and-validations)
- [Error Handling](#-error-handling)
- [Getting Started — Run it Locally](#-getting-started--run-it-locally)
- [Configuration Reference](#-configuration-reference)
- [Sample Data — Pre-Seeded Accounts](#-sample-data--pre-seeded-accounts)
- [Swagger / OpenAPI](#-swagger--openapi)
- [Testing](#-testing)
- [Roadmap](#-roadmap)
- [Author](#-author)
- [License](#-license)

---

## 🌐 What is this project?

This is a **backend REST API** — the server-side engine that powers a hospital management application used by receptionists, doctors, pharmacists, administrators and patients.

It handles everything behind the scenes:

- When a patient registers or logs in — this API takes care of it
- When someone books an appointment with a doctor — this API checks the slot is free and saves it
- When a patient walks in without an appointment — this API issues the next queue token
- When an ambulance brings in an emergency — this API puts the most critical patient first
- When the pharmacy dispenses medicine — this API checks stock and expiry, then updates inventory
- When a patient is admitted or discharged — this API locks and releases the bed
- When the administrator opens the dashboard — this API summarizes the day

> **For beginners:** A REST API is a set of URLs (called *endpoints*) that a frontend app, mobile app, or tool like Postman calls to get or send data. This project provides all of those endpoints for a hospital. There is no user interface in this repository — the interactive Swagger page is the "front door" for testing.

### 🚀 Key Highlights

- ✅ **Pure Backend REST API** — no frontend dependency, clean service boundaries
- ✅ **Stateless JWT Security** — Bearer token authentication with BCrypt password hashing
- ✅ **Role-Based Access Control** — `ROLE_ADMIN`, `ROLE_DOCTOR`, `ROLE_PATIENT`
- ✅ **16 API Domains** — each grouped separately in Swagger UI
- ✅ **Double-Booking Prevention** — no two appointments for the same doctor and slot
- ✅ **Sequential Walk-In Tokens** — `W-001`, `W-002`, ... reset every day
- ✅ **Emergency Triage Ordering** — `CRITICAL` > `HIGH` > `MEDIUM` > `LOW`
- ✅ **Pharmacy Safety Rules** — expired medicine and insufficient stock are rejected
- ✅ **Bed Lifecycle** — admission occupies a bed, discharge releases it automatically
- ✅ **Unified Medical History** — one endpoint returns a patient's complete timeline
- ✅ **Dual Database Mode** — MySQL for production, in-memory H2 for zero-setup development

---

## 🚀 Tech Stack

| Layer | Technology | What it does |
| :--- | :--- | :--- |
| Language | Java 17 LTS | The programming language used |
| Framework | Spring Boot 3.3.4 | Makes building Java web apps fast and easy |
| Web Layer | Spring Web MVC | Handles HTTP requests and responses |
| Security | Spring Security 6.x + JJWT 0.12.6 | Protects endpoints, issues and verifies login tokens |
| ORM | Spring Data JPA + Hibernate 6.5.3 | Connects Java code to the database |
| Database | MySQL 8.x (production) / H2 2.2.x (dev & test) | Stores all the data |
| Connection Pool | HikariCP | Manages database connections |
| Validation | Jakarta Bean Validation (Hibernate Validator) | Rejects bad input before it reaches business logic |
| API Docs | Springdoc OpenAPI 2.6.0 | Generates Swagger UI automatically |
| Monitoring | Spring Boot Actuator | Health check endpoint |
| Build Tool | Apache Maven 3.9+ | Manages dependencies and builds |
| Testing | JUnit 5, Mockito, Spring Security Test | Unit, service and security tests |

---

## ✨ Features

### 🔐 Authentication and Security
- ✅ **JWT Token Authentication** — login returns a Bearer token; protected routes require it
- ✅ **BCrypt Password Hashing** — passwords are never stored as plain text
- ✅ **Role-Based Access** — Admin, Doctor and Patient have different permissions
- ✅ **Stateless Sessions** — no server-side session; every request is verified from the token
- ✅ **Self Registration** — patients can create their own account

### 🧑‍⚕️ Patients, Doctors and Departments
- ✅ Patient records with demographics, allergies and contact details (e.g. `PAT-2026-00001`)
- ✅ Doctor registration by Admin, listing by department
- ✅ Doctor status tracking — `AVAILABLE`, `BUSY`, `ON_LEAVE`, `INACTIVE`
- ✅ Department management

### 📅 Appointments
- ✅ Book appointments with a doctor
- ✅ Double-booking collision guard
- ✅ Doctor schedule and patient history views
- ✅ Status flow — `CONFIRMED` → `CHECKED_IN` → `COMPLETED` / `CANCELLED`

### 🚶 Walk-In Visits
- ✅ Register patients without appointments
- ✅ Daily sequential token generation
- ✅ Queue by day and by department
- ✅ Status flow — `WAITING` → `CALLED` → `IN_CONSULTATION` → `COMPLETED`

### 🚑 Emergency Cases
- ✅ Register emergencies with vitals and triage category
- ✅ Active case list sorted by clinical priority
- ✅ Triage re-assessment and status transitions — `TRIAGE`, `UNDER_TREATMENT`, `ADMITTED`, `DISCHARGED`

### 🩺 Consultations and Prescriptions
- ✅ Record diagnosis, symptoms, vitals and clinical notes
- ✅ Issue digital prescriptions with medication, dosage and duration
- ✅ History by patient

### 💊 Pharmacy and Inventory
- ✅ Medicine batches with expiry date, stock and unit price
- ✅ Low-stock and expired-stock alerts
- ✅ Dispensing with automatic stock deduction
- ✅ Expired and insufficient-stock rejection

### 🛏️ Beds, Wards and Admissions
- ✅ Wards — General, ICU, Emergency, Private
- ✅ Available-bed lookup
- ✅ Admission atomically marks the bed `OCCUPIED`
- ✅ Discharge atomically marks the bed `AVAILABLE`

### 💳 Billing and Payments
- ✅ Itemized invoices — consultation, room, medicine, diagnostics
- ✅ Payments by Cash, Card, UPI or Insurance

### 📊 Messaging, Dashboard and Reports
- ✅ Department inquiries and feedback
- ✅ Dashboard — patients, doctors, appointments, beds, low-stock alerts
- ✅ Reports — daily patients, revenue, bed occupancy, pharmacy sales

---

## 🏗️ How it Works — Architecture

> **For beginners:** When you call this API, your request goes through several layers before it reaches the database. Think of a hospital reception: the **security guard** checks your ID (JWT filter), the **receptionist** takes your request (Controller), the **doctor** decides what is allowed (Service), and the **records room** stores the file (Repository / Database).

```text
🖥️ Client (Swagger UI / Postman / Mobile App / Web App)
           │
           │  HTTP Request  +  Authorization: Bearer <JWT>
           ▼
┌─────────────────────────────────────┐
│     JwtAuthenticationFilter         │  ← Checks the token before anything else
│     + Role-Based Access Control     │    (ADMIN / DOCTOR / PATIENT)
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│     Controller Layer                │  ← Receives request, validates input, calls service
│  (@RestController, /api/**)         │
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│     Service Layer                   │  ← All business rules live here
│  (@Service, @Transactional)         │    (double-booking, triage, stock, beds)
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│     Repository Layer                │  ← Talks to the database
│  (Spring Data JPA)                  │
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│     MySQL 8.x  /  H2 (in-memory)    │  ← Stores all the data
└─────────────────────────────────────┘
```

**Key design decisions explained simply:**

- **Controllers never contain business logic** — they pass requests to services and return responses
- **Services contain all the rules** — double-booking check, triage ordering, expiry check, bed collision guard
- **Transactions keep data consistent** — dispensing medicine or admitting a patient either fully succeeds or fully rolls back
- **Security runs first** — an unauthenticated request never reaches a controller
- **Custom exceptions** — each failure type has its own exception, so errors are clear and testable

---

## 📁 Project Structure

<!-- TODO: adjust package names and file lists to match the actual repository before publishing -->

```text
Hospital-Management-System/
│
├── src/main/java/com/lifecare/hospital/
│   │
│   ├── LifeCareHospitalApplication.java     ← Entry point — starts the app
│   │
│   ├── config/                              ← Security, OpenAPI and data seeding
│   │   ├── SecurityConfig.java              ← Filter chain, BCrypt bean, route protection
│   │   ├── OpenApiConfig.java               ← Swagger metadata and 16 domain groups
│   │   └── DataInitializer.java             ← Seeds default accounts and sample records
│   │
│   ├── security/                            ← Everything JWT related
│   │   ├── JwtService.java                  ← Generates and validates tokens (JJWT)
│   │   ├── JwtAuthenticationFilter.java     ← Runs on every request, checks the Bearer token
│   │   └── CustomUserDetailsService.java    ← Loads user + role for Spring Security
│   │
│   ├── controller/                          ← HTTP request handlers
│   │   ├── AuthController.java              ← /api/auth/**
│   │   ├── PatientController.java           ← /api/patients/**
│   │   ├── DoctorController.java            ← /api/doctors/**
│   │   ├── DepartmentController.java        ← /api/departments/**
│   │   ├── WalkInController.java            ← /api/walk-ins/**
│   │   ├── EmergencyController.java         ← /api/emergency/**
│   │   ├── AppointmentController.java       ← /api/appointments/**
│   │   ├── ConsultationController.java      ← /api/consultations/**
│   │   ├── PrescriptionController.java      ← /api/prescriptions/**
│   │   ├── PharmacyController.java          ← /api/inventory/** and /api/pharmacy/**
│   │   ├── BedController.java               ← /api/beds/** and /api/wards/**
│   │   ├── AdmissionController.java         ← /api/admissions/**
│   │   ├── BillingController.java           ← /api/billing/**
│   │   ├── MessageController.java           ← /api/messages/**
│   │   ├── DashboardController.java         ← /api/dashboard/**
│   │   └── ReportController.java            ← /api/reports/**
│   │
│   ├── service/                             ← Business logic layer (interfaces + *Impl classes)
│   │   ├── AppointmentServiceImpl.java      ← Double-booking guard
│   │   ├── WalkInService*.java              ← Daily token generator
│   │   ├── EmergencyService*.java           ← Triage priority ordering
│   │   ├── PharmacyService*.java            ← Stock + expiry validation
│   │   ├── AdmissionService*.java           ← Bed occupy / release
│   │   └── ...                              ← One service per domain
│   │
│   ├── repository/                          ← 21 Spring Data JPA interfaces
│   ├── entity/                              ← JPA entities (map to DB tables)
│   ├── dto/                                 ← Request and response objects
│   ├── enums/                               ← Roles, statuses, triage levels, payment methods
│   └── exception/                           ← Custom exceptions + GlobalExceptionHandler
│
├── src/main/resources/
│   └── application.yml                      ← App, profile, database and JWT configuration
│
├── src/test/java/                           ← JUnit 5 + Mockito + Spring Security tests
├── pom.xml                                  ← Maven dependencies
├── LICENSE
└── README.md
```

---

## 🗄️ Domain Model

> **For beginners:** A domain model describes the main "things" the system keeps track of and how they connect. Hibernate turns each entity into a database table automatically, so you do not need to create tables by hand.

<!-- TODO: replace with exact column lists from the entity classes if you want a per-table schema like the Social-Media README -->

```text
USER ──────┬── PATIENT ──┬── APPOINTMENT ──── DOCTOR ──── DEPARTMENT
           │             ├── WALK-IN VISIT ───────────────────┘
           └── DOCTOR    ├── EMERGENCY CASE
                         ├── CONSULTATION ──── PRESCRIPTION ──── MEDICINE (inventory)
                         ├── ADMISSION ──────── BED ──── WARD
                         └── INVOICE ──────── PAYMENT
```

| Entity | Purpose | Notable fields / values |
| :--- | :--- | :--- |
| User | Login identity | email, BCrypt password, role (`ADMIN`, `DOCTOR`, `PATIENT`) |
| Patient | Person receiving care | patient code (e.g. `PAT-2026-00001`), demographics, allergies, contact |
| Doctor | Clinician | department, specialty, status (`AVAILABLE`, `BUSY`, `ON_LEAVE`, `INACTIVE`) |
| Department | Hospital unit | name, doctors, queue |
| Appointment | Scheduled visit | doctor, patient, date/time, status (`CONFIRMED`, `CHECKED_IN`, `COMPLETED`, `CANCELLED`) |
| Walk-In Visit | Unscheduled visit | daily token (`W-001`), department, status (`WAITING`, `CALLED`, `IN_CONSULTATION`, `COMPLETED`) |
| Emergency Case | Urgent arrival | vitals, triage (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`), status (`TRIAGE`, `UNDER_TREATMENT`, `ADMITTED`, `DISCHARGED`) |
| Consultation | Clinical encounter | diagnosis, symptoms, vitals, notes |
| Prescription | Medication order | medicines, dosage, duration |
| Medicine (Inventory) | Pharmacy stock | batch, expiry date, quantity, unit price, minimum threshold |
| Ward / Bed | Inpatient capacity | ward type (General, ICU, Emergency, Private), bed status (`AVAILABLE`, `OCCUPIED`, maintenance) |
| Admission | Inpatient stay | patient, bed, admit/discharge dates, discharge summary |
| Invoice / Payment | Billing | itemized charges, payment method (Cash, Card, UPI, Insurance) |
| Message | Department inquiry | department, content, read flag |

---

## 📡 API Reference

### Base URL

```text
http://localhost:8085
```

> All endpoints except **register**, **login**, Swagger and the health check require `Authorization: Bearer <token>`.

### 🔑 Authentication — No token needed for register / login

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new patient account | Public |
| `POST` | `/api/auth/login` | Login and receive a JWT | Public |
| `GET` | `/api/auth/me` | Current authenticated user profile | Any logged-in user |

### 🧑‍🤝‍🧑 Patients

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/patients` | List all patients | Admin / Doctor |
| `GET` | `/api/patients/{id}` | Get patient by ID | Authenticated |
| `POST` | `/api/patients` | Create patient record | Authenticated |
| `PUT` | `/api/patients/{id}` | Update demographics, allergies, contact | Authenticated |
| `GET` | `/api/patients/{id}/medical-history` | Unified timeline of every encounter, prescription, admission and bill | Authenticated |

### 🩺 Doctors

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/doctors` | List all doctors | Authenticated |
| `GET` | `/api/doctors/{id}` | Get doctor profile | Authenticated |
| `GET` | `/api/doctors/department/{deptId}` | Doctors in a department | Authenticated |
| `POST` | `/api/doctors` | Register a new doctor | Admin |
| `PUT` | `/api/doctors/{id}/status` | Set `AVAILABLE`, `BUSY`, `ON_LEAVE`, `INACTIVE` | Authenticated |

### 🏢 Departments

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/departments` | List departments | Authenticated |
| `GET` | `/api/departments/{id}` | Get department | Authenticated |
| `POST` | `/api/departments` | Create department | Admin |
| `PUT` | `/api/departments/{id}` | Update department | Admin |

### 🚶 Walk-In Visits

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/walk-ins` | Register arrival; returns daily token `W-001`, `W-002`, ... |
| `GET` | `/api/walk-ins/today` | Today's active queue |
| `GET` | `/api/walk-ins/department/{deptId}/queue` | Queue for one department |
| `PUT` | `/api/walk-ins/{id}/status` | `WAITING` → `CALLED` → `IN_CONSULTATION` → `COMPLETED` |

### 🚑 Emergency Cases

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/emergency` | Register emergency with vitals and triage |
| `GET` | `/api/emergency/active` | Active cases, most critical first |
| `PUT` | `/api/emergency/{id}/triage` | Change triage severity |
| `PUT` | `/api/emergency/{id}/status` | `TRIAGE`, `UNDER_TREATMENT`, `ADMITTED`, `DISCHARGED` |

### 📅 Appointments

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/appointments` | Book appointment (double-booking guard) | `201` / `409` |
| `GET` | `/api/appointments/doctor/{doctorId}` | Doctor's schedule | `200` |
| `GET` | `/api/appointments/patient/{patientId}` | Patient's history | `200` |
| `PUT` | `/api/appointments/{id}/status` | `CONFIRMED`, `CHECKED_IN`, `COMPLETED`, `CANCELLED` | `200` |

### 📝 Consultations and Prescriptions

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/consultations` | Record diagnosis, vitals, symptoms, notes |
| `GET` | `/api/consultations/{id}` | Get consultation report |
| `GET` | `/api/consultations/patient/{patientId}` | Patient consultation history |
| `POST` | `/api/prescriptions` | Issue prescription (medicines, dosage, duration) |
| `GET` | `/api/prescriptions/{id}` | Get prescription |
| `GET` | `/api/prescriptions/patient/{patientId}` | Patient prescription history |

### 💊 Pharmacy and Inventory

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/inventory` | View medicine inventory |
| `POST` | `/api/inventory` | Add medicine batch (expiry, stock, unit price) |
| `GET` | `/api/inventory/low-stock` | Medicines below minimum threshold |
| `GET` | `/api/inventory/expired` | Expired stock |
| `POST` | `/api/pharmacy/sales` | Dispense medicine: validates stock and expiry, then deducts quantity |

### 🛏️ Beds, Wards and Admissions

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/wards` | List wards (General, ICU, Emergency, Private) |
| `GET` | `/api/beds/available` | Beds ready for admission |
| `PUT` | `/api/beds/{id}/status` | Manually set bed availability / maintenance |
| `POST` | `/api/admissions` | Admit patient; bed becomes `OCCUPIED` atomically |
| `GET` | `/api/admissions/active` | Currently admitted inpatients |
| `PUT` | `/api/admissions/{id}/discharge` | Discharge; bed returns to `AVAILABLE` atomically |

### 💳 Billing

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/billing/invoices` | Generate itemized invoice |
| `GET` | `/api/billing/invoices/{id}` | Fetch invoice |
| `POST` | `/api/billing/invoices/{id}/payments` | Record payment (Cash, Card, UPI, Insurance) |

### 💌 Messaging

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/messages` | Send inquiry or feedback to a department | Authenticated |
| `GET` | `/api/messages` | List messages | Admin / Doctor |
| `PUT` | `/api/messages/{id}/read` | Mark message as read | Admin / Doctor |

### 📊 Dashboard and Reports

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/dashboard/summary` | Patients, active doctors, today's appointments, free beds, low-stock alerts |
| `GET` | `/api/dashboard/walk-ins` | Walk-in statistics |
| `GET` | `/api/dashboard/emergency` | Emergency breakdown by triage category |
| `GET` | `/api/reports/daily-patients` | Daily arrivals across departments |
| `GET` | `/api/reports/revenue` | Revenue by date range and payment method |
| `GET` | `/api/reports/bed-occupancy` | Ward occupancy rates |
| `GET` | `/api/reports/pharmacy-sales` | Dispensed volume and totals |

### 🩺 Monitoring

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/actuator/health` | Application health |

---

## 📦 How to Use the API — Request and Response Examples

> **For beginners:** Follow these steps in order. First you log in and get a token, then you use that token for everything else.
>
> <!-- TODO: confirm the exact response wrapper and field names against the real controllers; the shapes below are illustrative -->
> The JSON below shows the **shape** of typical requests and responses. Exact field names may differ slightly; Swagger UI always shows the real schema.

### Step 1 — Login as the seeded Admin

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "email": "admin@hms.com",
  "password": "Admin@123"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "email": "admin@hms.com",
    "role": "ROLE_ADMIN"
  }
}
```

> ⚠️ **Important:** Copy the `token`. Send it on every other request like this:
>
> `Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...`

### Step 2 — Register a Walk-In Patient

```http
POST /api/walk-ins
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "patientId": 1,
  "departmentId": 1,
  "complaint": "Fever and headache"
}
```

**Response (illustrative):**

```json
{
  "id": 6,
  "token": "W-006",
  "status": "WAITING",
  "departmentId": 1
}
```

### Step 3 — Book an Appointment

```http
POST /api/appointments
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDate": "2026-10-05",
  "appointmentTime": "10:30"
}
```

Booking the **same doctor at the same date and time again** is rejected:

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Doctor already has an appointment at this time"
}
```

### Step 4 — Emergency Queue (Most Critical First)

```http
GET /api/emergency/active
Authorization: Bearer <token>
```

Cases come back ordered: all `CRITICAL`, then `HIGH`, then `MEDIUM`, then `LOW`.

### Step 5 — Dispense Medicine

```http
POST /api/pharmacy/sales
Authorization: Bearer <token>
Content-Type: application/json
```

If the medicine is expired or stock is too low, nothing is deducted and an error is returned (see [Error Handling](#-error-handling)).

### Step 6 — Admit and Discharge

```http
POST /api/admissions                      → bed becomes OCCUPIED
PUT  /api/admissions/{id}/discharge       → bed becomes AVAILABLE
```

### Step 7 — Full Medical History of a Patient

```http
GET /api/patients/1/medical-history
Authorization: Bearer <token>
```

Returns one timeline combining appointments, walk-ins, emergency visits, consultations, prescriptions, admissions and bills.

---

## 🔒 Authentication — How JWT Works in this Project

> **For beginners:** JWT (JSON Web Token) is like a digital hospital ID badge. When you log in, the server gives you a badge. You show the badge on every request to prove who you are and what you are allowed to do. The server does not need to remember you — the badge carries the information.

```text
1. User sends POST /api/auth/login with email and password
         │
         ▼
2. Spring Security verifies the password using BCrypt
         │
         ▼
3. JwtService generates a signed token containing the user's identity and role
         │
         ▼
4. Token is returned to the client
         │
         ▼
5. Client sends it on every request:
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
         │
         ▼
6. JwtAuthenticationFilter intercepts the request
         │
         ▼
7. Filter extracts the token and JwtService validates it
   (is the signature valid? has it expired?)
         │
         ▼
8. Valid   → user and role are placed in the security context → request reaches the Controller
   Invalid → request is rejected
```

**Roles**

| Role | Typical use |
| :--- | :--- |
| `ROLE_ADMIN` | Registers doctors, manages departments, beds, billing, reports and the dashboard |
| `ROLE_DOCTOR` | Consultations, prescriptions, triage, viewing patients and messages |
| `ROLE_PATIENT` | Own appointments and medical history |

**Token expiry:** 24 hours (`86400000` ms), configurable with `JWT_EXPIRATION_MS`.

**Authorizing in Swagger UI:**

1. Call `POST /api/auth/login` with a seeded account
2. Copy the `token` value from the response
3. Click the green **Authorize** button at the top right
4. Paste the token into the **Value** box and click **Authorize**
5. Every protected endpoint now works from the browser

---

## 🧠 Business Rules and Validations

> All business rules are enforced in the **service layer**, never in controllers. This keeps controllers clean and the rules easy to unit test.
> The snippets below are simplified to show the idea; see the matching `*ServiceImpl` classes for the real code.

### Appointment Double-Booking Guard

```java
// AppointmentServiceImpl.java
boolean slotTaken = appointmentRepository
        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(doctorId, date, time);
if (slotTaken) {
    throw new DoubleBookingException("Doctor already has an appointment at this time");  // 409
}
```

### Sequential Daily Walk-In Token

```java
// WalkInService
long todayCount = walkInRepository.countByVisitDate(LocalDate.now());
String token = String.format("W-%03d", todayCount + 1);   // W-001, W-002, ...
```

### Emergency Triage Ordering

```text
EmergencyCasePriority order:

CRITICAL  >  HIGH  >  MEDIUM  >  LOW
   ▲
   └── patients needing immediate resuscitation are always shown first
```

### Pharmacy Expiry and Stock Invariant

Both checks happen **before** any stock is changed.

```java
// PharmacyService
if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
    throw new ExpiredMedicineException("Medicine is expired");
}
if (medicine.getQuantity() < requestedQuantity) {
    throw new InsufficientStockException("Not enough stock available");
}
medicine.setQuantity(medicine.getQuantity() - requestedQuantity);
```

### Bed Occupancy Lifecycle

```text
Bed AVAILABLE ──admit patient──▶ Bed OCCUPIED ──discharge──▶ Bed AVAILABLE
                    │
                    └── bed already OCCUPIED?  → BedUnavailableException
```

### Input Validation

Jakarta annotations (`@NotBlank`, `@Email`, `@NotNull`, `@Size`, ...) on request objects reject bad payloads with `400 BAD REQUEST` before the service layer runs.

---

## ⚠️ Error Handling

A `GlobalExceptionHandler` catches exceptions from every layer and returns clean JSON, so clients never see raw Java stack traces.

<!-- TODO: confirm HTTP status for each custom exception against GlobalExceptionHandler; only 403 and the double-booking 409 are confirmed by the existing tests/docs -->

### Complete Error Reference

| Scenario | Exception | HTTP Status |
| :--- | :--- | :---: |
| Doctor already booked at that date/time | `DoubleBookingException` | `409 CONFLICT` |
| Medicine is expired | `ExpiredMedicineException` | `400 BAD REQUEST` |
| Not enough medicine stock | `InsufficientStockException` | `400 BAD REQUEST` |
| Bed already occupied | `BedUnavailableException` | `409 CONFLICT` |
| Record not found | Resource-not-found exception | `404 NOT FOUND` |
| Invalid request body | Validation exception | `400 BAD REQUEST` |
| Wrong email or password | Bad credentials | `401 UNAUTHORIZED` |
| Anonymous or unauthorized user hits a protected admin resource | Spring Security | `403 FORBIDDEN` |
| Unexpected server error | `Exception` | `500 INTERNAL SERVER ERROR` |

---

## ⚙️ Getting Started — Run it Locally

> **For beginners:** Follow these steps in order. The fastest path uses the in-memory H2 database, so you do not need to install MySQL at all.

### What you need installed first

- **Java 17 or higher** — verify with `java -version`
- **Maven 3.8+** — verify with `mvn -version`
- **MySQL 8.x** — optional, only for production mode
- **Postman** — optional, for testing (Swagger UI works too)
- **IntelliJ IDEA** — recommended IDE

### Step 1 — Clone the repository

```bash
git clone https://github.com/Xaryansonawane/Hospital-Management-System.git
cd Hospital-Management-System
```

### Step 2 — Build and test

```bash
mvn clean test                       # compile and run all tests
mvn clean package -DskipTests        # build the executable JAR
```

### Step 3 — Run the application

#### Option A — H2 in-memory (zero setup, recommended for first run)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

Or with the packaged JAR:

```bash
java -jar target/hospital-backend-1.0.0.jar --spring.profiles.active=h2
```

#### Option B — MySQL (production mode)

Make sure MySQL is running on port `3306`, then:

```bash
java -jar target/hospital-backend-1.0.0.jar
```

With custom database credentials:

```bash
# Linux / macOS
export DB_HOST=localhost DB_PORT=3306 DB_NAME=lifecare_hospital
export DB_USERNAME=root DB_PASSWORD=your_password
java -jar target/hospital-backend-1.0.0.jar
```

```powershell
# Windows PowerShell
$env:DB_HOST="localhost"; $env:DB_PORT="3306"; $env:DB_NAME="lifecare_hospital"
$env:DB_USERNAME="root"; $env:DB_PASSWORD="your_password"
java -jar target/hospital-backend-1.0.0.jar
```

### Step 4 — Test the API

The API is now live at `http://localhost:8085`.

```bash
curl http://localhost:8085/actuator/health
```

Then log in:

```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@hms.com","password":"Admin@123"}'
```

Or open Swagger UI: <http://localhost:8085/swagger-ui/index.html>

---

## 🔧 Configuration Reference

The app is configured through `src/main/resources/application.yml`, with environment variable overrides.

| Variable | Default | Description |
| :--- | :--- | :--- |
| `PORT` | `8085` | HTTP server port |
| `SPRING_PROFILES_ACTIVE` | `mysql` | Active profile: `mysql` or `h2` |
| `DB_HOST` | `localhost` | MySQL hostname |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `lifecare_hospital` | Database name |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | `root` | Database password |
| `JWT_SECRET` | built-in default (Base64) | 256-bit secret used to sign tokens |
| `JWT_EXPIRATION_MS` | `86400000` | Token lifetime in milliseconds (24 hours) |

> 🔴 **Security warning:** The default `JWT_SECRET`, the default database password and the seeded account passwords exist **for local development only**. Before any shared or public deployment, set your own `JWT_SECRET`, `DB_PASSWORD`, and change or remove the seeded accounts. Anyone who knows the default secret can forge valid tokens.

---

## 🌱 Sample Data — Pre-Seeded Accounts

On startup the application seeds initial records so you can try it immediately.

| Role | Email | Password | What this account is for |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@hms.com` | `Admin@123` | Full control: doctors, beds and wards, billing, reports, dashboard |
| **DOCTOR** | `dr.smith@lifecare.com` | `Doctor@123` | Cardiology specialist: consultations, prescriptions, triage |
| **DOCTOR** | `dr.sarah@lifecare.com` | `Doctor@123` | Pediatrics specialist: appointments and inpatient treatment |
| **PATIENT** | `jane.doe@example.com` | `Patient@123` | Registered patient `PAT-2026-00001`: bookings and medical history |

---

## 📖 Swagger / OpenAPI

| Resource | URL |
| :--- | :--- |
| Interactive Swagger UI | <http://localhost:8085/swagger-ui/index.html> |
| Raw OpenAPI JSON | <http://localhost:8085/v3/api-docs> |
| Health check | <http://localhost:8085/actuator/health> |

Swagger UI organizes the API into **16 domain groups** and lets you test every endpoint from the browser once you click **Authorize** with your token.

---

## 🧪 Testing

The project uses **JUnit 5, Mockito and Spring Security Test**.

```bash
mvn test
```

**Test classes and what they verify**

| Test class | Verifies |
| :--- | :--- |
| `LifeCareHospitalApplicationTests` | Spring context loads and default data is seeded |
| `SecurityAuthorizationTest` | Anonymous / unauthorized users get `403` on protected admin resources; authenticated admins get `200` |
| `HospitalWorkflowsServiceTest` | Business workflows, listed below |

**Workflow tests**

- ✅ **Appointments** — duplicate booking on the same doctor and time is blocked
- ✅ **Walk-ins** — tokens are sequential (e.g. next token `W-006`)
- ✅ **Emergency** — `CRITICAL` cases are listed first
- ✅ **Pharmacy** — expired medicine and insufficient stock are rejected
- ✅ **Admissions** — occupied bed allocation is rejected; discharge frees the bed

---

## 🛣️ Roadmap

Things planned for future versions:

- [ ] **Return `401` for missing tokens** — unauthenticated requests currently surface as `403`; add a custom authentication entry point with a clean JSON body
- [ ] **Concurrency-safe walk-in tokens** — add a unique constraint on (date, token) so two simultaneous arrivals can never receive the same number
- [ ] **Appointment slot constraint** — database-level unique index on (doctor, date, time) as a second line of defence behind the service check
- [ ] **Pagination and sorting** on patient, appointment, and inventory lists
- [ ] **Refresh tokens** — keep users logged in without re-entering credentials
- [ ] **Audit trail** — record who changed what and when (prescriptions, billing, bed status)
- [ ] **Flyway migrations** — replace automatic schema updates in production
- [ ] **Docker and docker-compose** — one-command setup with MySQL
- [ ] **Integration tests** with MockMvc / Testcontainers
- [ ] **Email and SMS reminders** for appointments
- [ ] **Insurance claim workflow** on top of the existing payment methods
- [ ] **Lab and diagnostics module**

---

## 👨‍💻 Author

**Aryan Sonawane**

Computer Engineering student and Java backend developer passionate about building real-world projects with Spring Boot.

- GitHub: [@Xaryansonawane](https://github.com/Xaryansonawane)
- LinkedIn: [Xaryansonawane](https://www.linkedin.com/in/Xaryansonawane)
- LeetCode: [Xaryansonawane](https://leetcode.com/Xaryansonawane)

---

## 📄 License

This project is open source and available under the [Apache License 2.0](LICENSE).

---

⭐ If you found this project helpful or learned something from it, please give it a star on GitHub — it really helps!
