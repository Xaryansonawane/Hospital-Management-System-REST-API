# Hospital Management System REST API

[![Java 17](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue.svg)](https://spring.io/projects/spring-security)
[![OpenAPI / Swagger](https://img.shields.io/badge/OpenAPI-3.0-green.svg)](http://localhost:8085/swagger-ui/index.html)
[![License](https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg)](LICENSE)

An enterprise-grade, **Backend-Only REST API** Hospital Management System engineered with **Java 17, Spring Boot 3.3.4, Spring Data JPA, Hibernate, Spring Security, JWT, MySQL/H2, and Springdoc OpenAPI (Swagger UI)**.

---

## Key Highlights

- **Pure Backend REST API**: Zero frontend dependencies. Designed for enterprise scalability, high-availability, and clean service boundaries.
- **Interactive Documentation**: Springdoc OpenAPI / Swagger UI pre-configured at `http://localhost:8085/swagger-ui/index.html` organized into 16 domain groups.
- **Enterprise Security**: Stateless JWT authentication with Bearer token header, BCrypt password hashing, and role-based access control (`ROLE_ADMIN`, `ROLE_DOCTOR`, `ROLE_PATIENT`).
- **Comprehensive Clinical & Operational Modules**:
  1. **Walk-In Visits** with sequential daily token generation (`W-001`, `W-002`, ...).
  2. **Emergency Cases** with automated clinical triage priority sorting (`CRITICAL` > `HIGH` > `MEDIUM` > `LOW`).
  3. **Doctor Appointments** with double-booking collision prevention.
  4. **Pharmacy & Inventory** with real-time stock deductions, expired medicine rejection, and batch tracking.
  5. **Inpatient Admissions & Bed Allocation** with real-time bed occupancy locking and automatic release upon discharge.
  6. **Unified Patient Medical History** aggregating all clinical encounters, vitals, prescriptions, admissions, and bills.
  7. **Department Messaging, Invoicing & Payments, and Management Dashboards & Reports**.

---

## System Architecture

```
+-------------------------------------------------------------------------------------------------+
|                                     Client Applications                                         |
|                 (REST API Clients, Mobile Apps, Swagger UI, Third-Party Systems)                |
+-----------------------------------------------+-------------------------------------------------+
                                                |
                                    HTTP Requests (Port 8085)
                                    Authorization: Bearer <JWT>
                                                |
+-----------------------------------------------v-------------------------------------------------+
|                                 Spring Boot Application Layer                                   |
|                                                                                                 |
|  +----------------------------------+     +--------------------------------------------------+  |
|  |     Security & Filter Chain      |     |              OpenAPI Documentation               |  |
|  |  - JwtAuthenticationFilter       |     |  - Springdoc Swagger UI (/swagger-ui/index.html) |  |
|  |  - CustomUserDetailsService      |     |  - OpenAPI JSON Spec (/v3/api-docs)              |  |
|  |  - Role-Based Access (RBAC)      |     |  - 16 Modular Swagger Domain Groups              |  |
|  +----------------------------------+     +--------------------------------------------------+  |
|                                                                                                 |
|  +-------------------------------------------------------------------------------------------+  |
|  |                                  REST Controllers (/api/**)                                |  |
|  |  AuthController       | PatientController     | DoctorController     | DepartmentController|  |
|  |  WalkInController     | EmergencyController   | AppointmentController| ConsultationCtrl    |  |
|  |  PrescriptionCtrl     | PharmacyController    | BedController        | AdmissionController |  |
|  |  BillingController    | MessageController     | DashboardController  | ReportController    |  |
|  +-------------------------------------------------------------------------------------------+  |
|                                                                                                 |
|  +-------------------------------------------------------------------------------------------+  |
|  |                               Service Layer & Business Rules                              |  |
|  |  - Double-booking validation                  - Sequential token generation (W-001)       |  |
|  |  - Triage priority ranking (CRITICAL first)   - Expired & insufficient stock rejection    |  |
|  |  - Bed collision prevention & auto-release    - Comprehensive medical timeline aggregator |  |
|  +-------------------------------------------------------------------------------------------+  |
|                                                                                                 |
|  +-------------------------------------------------------------------------------------------+  |
|  |                             Data Access & Persistence Layer                               |  |
|  |  - Spring Data JPA Repositories (21 interfaces)                                           |  |
|  |  - Hibernate ORM with optimistic / pessimistic locking                                     |  |
|  +-------------------------------------------------------------------------------------------+  |
+-----------------------------------------------+-------------------------------------------------+
                                                |
                                     JDBC Connection (HikariCP)
                                                |
+-----------------------------------------------v-------------------------------------------------+
|                                Database Persistence Layer                                       |
|               MySQL 8.x (Production)  /  H2 In-Memory Database (Dev & Testing)                   |
+-------------------------------------------------------------------------------------------------+
```

---

## Technology Stack

| Component | Technology | Version |
| :--- | :--- | :--- |
| **Language** | Java (OpenJDK / Microsoft JDK) | 17 LTS |
| **Framework** | Spring Boot | 3.3.4 |
| **Web Layer** | Spring Web MVC | 3.3.4 |
| **Security** | Spring Security + JJWT | 6.x / 0.12.6 |
| **Persistence** | Spring Data JPA + Hibernate ORM | 3.3.4 / 6.5.3 |
| **Databases** | MySQL (Default/Prod) & H2 (In-memory Dev/Test) | 8.x / 2.2.x |
| **Validation** | Jakarta Bean Validation (Hibernate Validator) | 3.0.x |
| **API Docs** | Springdoc OpenAPI (Swagger UI) | 2.6.0 |
| **Monitoring** | Spring Boot Actuator | 3.3.4 |
| **Build Tool** | Apache Maven | 3.9+ |
| **Testing** | JUnit 5 + Mockito + Spring Security Test | Latest |

---

## Prerequisites & Installation

### Prerequisites
- **Java Development Kit (JDK) 17 or higher**
- **Apache Maven 3.8+** (or Maven wrapper)
- **MySQL 8.x** (optional when running in dev/test mode with H2)

### Clone & Build
```bash
# Navigate to the repository root
cd Hospital-Management-System-main

# Compile and run unit tests
mvn clean test

# Build executable production JAR
mvn clean package -DskipTests
```

---

## Configuration & Environment Variables

The application is configured via `src/main/resources/application.yml`. All sensitive database connection parameters can be provided via standard environment variables or overridden directly:

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `PORT` | HTTP Server Port | `8085` |
| `SPRING_PROFILES_ACTIVE` | Active Spring Profile (`mysql` or `h2`) | `mysql` |
| `DB_HOST` | MySQL Server Hostname | `localhost` |
| `DB_PORT` | MySQL Port | `3306` |
| `DB_NAME` | Database Name | `lifecare_hospital` |
| `DB_USERNAME` | Database User | `root` |
| `DB_PASSWORD` | Database Password | `root` |
| `JWT_SECRET` | 256-bit Secret Key for signing JWTs | Base64-encoded default key |
| `JWT_EXPIRATION_MS` | JWT validity duration (milliseconds) | `86400000` (24 Hours) |

---

## Running the Application

### 1. In-Memory Mode (Zero Database Setup - H2 Profile)
Ideal for testing, development, and immediate evaluation:
```powershell
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=h2

# Or running the packaged JAR directly
java -jar target/hospital-backend-1.0.0.jar --spring.profiles.active=h2
```

### 2. Production Mode (MySQL Database)
Ensure MySQL is running on port 3306, then execute:
```powershell
# Using default environment variables
java -jar target/hospital-backend-1.0.0.jar

# Or passing custom database credentials
$env:DB_HOST="localhost"; $env:DB_PORT="3306"; $env:DB_NAME="lifecare_hospital"; $env:DB_USERNAME="root"; $env:DB_PASSWORD="your_password"; java -jar target/hospital-backend-1.0.0.jar
```

---

## Swagger UI & API Documentation

When the application is running, access the interactive OpenAPI documentation:

- **Interactive Swagger UI**: [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)
- **Raw OpenAPI JSON Spec**: [http://localhost:8085/v3/api-docs](http://localhost:8085/v3/api-docs)
- **Actuator Health Endpoint**: [http://localhost:8085/actuator/health](http://localhost:8085/actuator/health)

### Authorizing in Swagger UI
1. Execute `POST /api/auth/login` using the pre-seeded credentials below.
2. Copy the `token` string from the JSON response.
3. Click the green **Authorize** button at the top right of the Swagger UI page.
4. Enter the token in the `Value:` input box and click **Authorize**.
5. All protected endpoints are now accessible directly within Swagger UI!

---

## Pre-Seeded Default Accounts

The application automatically seeds essential initial records upon startup:

| Role | Email | Password | Pre-Configured Capabilities |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@hms.com` | `Admin@123` | Full administrative control, doctor registrations, bed & ward management, billing, reports, dashboard |
| **DOCTOR** | `dr.smith@lifecare.com` | `Doctor@123` | Cardiology specialist, consultation notes, prescriptions, triage assignment |
| **DOCTOR** | `dr.sarah@lifecare.com` | `Doctor@123` | Pediatrics specialist, appointments & inpatient treatment |
| **PATIENT** | `jane.doe@example.com` | `Patient@123` | Registered patient (`PAT-2026-00001`), appointment bookings, medical history |

---

## REST API Reference (16 Domains)

### 1. Authentication (`/api/auth/**`)
- `POST /api/auth/register` - Register a new patient account.
- `POST /api/auth/login` - Authenticate with email/password; returns JWT token and user profile.
- `GET /api/auth/me` - Retrieve current authenticated user profile.

### 2. Patients (`/api/patients/**`)
- `GET /api/patients` - List all patients (Admin/Doctor).
- `GET /api/patients/{id}` - Get patient details by ID.
- `POST /api/patients` - Create patient record.
- `PUT /api/patients/{id}` - Update patient demographics, allergies, and contact details.
- `GET /api/patients/{id}/medical-history` - Unified medical timeline (appointments, walk-ins, emergency visits, consultations, prescriptions, admissions, bills).

### 3. Doctors (`/api/doctors/**`)
- `GET /api/doctors` - List all doctors.
- `GET /api/doctors/{id}` - Get doctor profile.
- `GET /api/doctors/department/{deptId}` - List doctors by department.
- `POST /api/doctors` - Register new doctor (Admin only).
- `PUT /api/doctors/{id}/status` - Update doctor status (`AVAILABLE`, `BUSY`, `ON_LEAVE`, `INACTIVE`).

### 4. Departments (`/api/departments/**`)
- `GET /api/departments` - List all hospital departments.
- `GET /api/departments/{id}` - Get department by ID.
- `POST /api/departments` - Create new department (Admin only).
- `PUT /api/departments/{id}` - Update department (Admin only).

### 5. Walk-In Visits (`/api/walk-ins/**`)
- `POST /api/walk-ins` - Register walk-in arrival; generates daily formatted token (`W-001`, `W-002`, ...).
- `GET /api/walk-ins/today` - Active queue for today.
- `GET /api/walk-ins/department/{deptId}/queue` - Queue filtered by department.
- `PUT /api/walk-ins/{id}/status` - Update status (`WAITING` -> `CALLED` -> `IN_CONSULTATION` -> `COMPLETED`).

### 6. Emergency Cases (`/api/emergency/**`)
- `POST /api/emergency` - Register incoming emergency with vitals and triage category.
- `GET /api/emergency/active` - List active cases ordered by priority: `CRITICAL` > `HIGH` > `MEDIUM` > `LOW`.
- `PUT /api/emergency/{id}/triage` - Update triage severity level.
- `PUT /api/emergency/{id}/status` - Transition status (`TRIAGE`, `UNDER_TREATMENT`, `ADMITTED`, `DISCHARGED`).

### 7. Appointments (`/api/appointments/**`)
- `POST /api/appointments` - Book appointment with double-booking prevention guard.
- `GET /api/appointments/doctor/{doctorId}` - Doctor's appointment schedule.
- `GET /api/appointments/patient/{patientId}` - Patient's appointment history.
- `PUT /api/appointments/{id}/status` - Update status (`CONFIRMED`, `CHECKED_IN`, `COMPLETED`, `CANCELLED`).

### 8. Consultations (`/api/consultations/**`)
- `POST /api/consultations` - Record diagnosis, vitals, symptoms, and clinical notes.
- `GET /api/consultations/{id}` - Get consultation report.
- `GET /api/consultations/patient/{patientId}` - Consultation history for patient.

### 9. Prescriptions (`/api/prescriptions/**`)
- `POST /api/prescriptions` - Issue digital prescription with medications, dosage, and duration.
- `GET /api/prescriptions/{id}` - Retrieve prescription.
- `GET /api/prescriptions/patient/{patientId}` - Prescriptions history for patient.

### 10. Pharmacy & Inventory (`/api/inventory/**` & `/api/pharmacy/**`)
- `GET /api/inventory` - View medicine inventory.
- `POST /api/inventory` - Add new medicine batch with expiry date, stock, and unit prices.
- `GET /api/inventory/low-stock` - Alert on medicines below minimum threshold.
- `GET /api/inventory/expired` - Identify expired stock.
- `POST /api/pharmacy/sales` - Process medication dispensing: validates stock availability, rejects expired items, and automatically updates quantities.

### 11. Beds & Wards (`/api/beds/**` & `/api/wards/**`)
- `GET /api/wards` - List hospital wards (General, ICU, Emergency, Private).
- `GET /api/beds/available` - Filter beds currently unoccupied and ready for admission.
- `PUT /api/beds/{id}/status` - Manually update bed maintenance/availability state.

### 12. Admissions (`/api/admissions/**`)
- `POST /api/admissions` - Admit patient to an available bed; atomically changes bed status to `OCCUPIED`.
- `GET /api/admissions/active` - List all currently admitted inpatients.
- `PUT /api/admissions/{id}/discharge` - Process discharge summary and atomically releases bed back to `AVAILABLE`.

### 13. Billing & Invoices (`/api/billing/**`)
- `POST /api/billing/invoices` - Generate itemized hospital invoice (Consultation, Room, Medicine, Diagnostic charges).
- `GET /api/billing/invoices/{id}` - Fetch invoice details.
- `POST /api/billing/invoices/{id}/payments` - Record payment (Cash, Card, UPI, Insurance).

### 14. Messaging (`/api/messages/**`)
- `POST /api/messages` - Submit inquiry or feedback mapped to hospital departments.
- `GET /api/messages` - List messages (Admin/Doctor).
- `PUT /api/messages/{id}/read` - Mark message as read.

### 15. Dashboard (`/api/dashboard/**`)
- `GET /api/dashboard/summary` - High-level metrics: total patients, active doctors, today's appointments, available beds, low-stock alerts.
- `GET /api/dashboard/walk-ins` - Real-time statistics on walk-in visits.
- `GET /api/dashboard/emergency` - Emergency admissions breakdown by triage category.

### 16. Reports (`/api/reports/**`)
- `GET /api/reports/daily-patients` - Daily patient arrival counts across departments.
- `GET /api/reports/revenue` - Revenue aggregate filtered by date range and payment method.
- `GET /api/reports/bed-occupancy` - Real-time ward occupancy rates.
- `GET /api/reports/pharmacy-sales` - Dispensed medication volume and financial totals.

---

## Core Business Rules & Invariants

1. **Appointment Double-Booking Guard**:
   Doctor schedule conflicts are intercepted in `AppointmentServiceImpl`. If an appointment already exists for the doctor at that date/time slot, it immediately throws `DoubleBookingException` (HTTP 409 Conflict).
2. **Deterministic Walk-In Token Generator**:
   Walk-in tokens follow a strict daily sequence (`W-001`, `W-002`, `W-003`, ...) calculated by counting today's existing entries atomically.
3. **Emergency Clinical Triage Sorting**:
   Active emergency queries prioritize cases using `EmergencyCasePriority` enumeration order: `CRITICAL` > `HIGH` > `MEDIUM` > `LOW`, ensuring patients requiring immediate resuscitation are presented first.
4. **Pharmacy Expiry & Stock Invariant**:
   Dispensing transactions enforce that medications must not be expired (`medicine.expiryDate >= LocalDate.now()`) and sufficient inventory must exist. Otherwise, `ExpiredMedicineException` or `InsufficientStockException` is thrown before any stock changes are committed.
5. **Bed Occupancy Lifecycle**:
   Assigning an occupied bed fails with `BedUnavailableException`. Admitting a patient flags the target bed as `OCCUPIED`. When the patient is discharged, the bed is automatically transitioned back to `AVAILABLE`.

---

## Automated Test Suite

Run the full automated test suite containing unit, service workflow, and security tests:

```powershell
mvn test
```

### Verified Test Cases:
- **`LifeCareHospitalApplicationTests`**: Verifies full Spring application context loads and data initialization seeds default entities.
- **`SecurityAuthorizationTest`**: Verifies that anonymous and unauthorized users receive `403 Forbidden` on protected admin resources, while authenticated admins receive `200 OK`.
- **`HospitalWorkflowsServiceTest`**:
  - `AppointmentService`: Prevents duplicate booking collisions on the same doctor/time.
  - `WalkInService`: Generates sequential token numbers (`W-006`).
  - `EmergencyService`: Sorts emergency cases with `CRITICAL` triage priority first.
  - `PharmacyService`: Rejects dispensing expired drugs and insufficient stock.
  - `AdmissionService`: Rejects occupied bed allocation and automatically frees bed upon patient discharge.

---

## License

This project is licensed under the Apache License 2.0.
