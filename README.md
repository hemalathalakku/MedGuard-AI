# MedGuard AI
**Intelligent Medicine Safety & Drug Interaction Detection System**

A complete Java web application that tells a user whether a medicine is safe
for them *before* they take it, by checking it against their diseases, current
medicines and allergies.

> ⚠️ Educational project. MedGuard AI does not provide medical advice.
> Always consult a doctor or pharmacist.

---

## Tech stack
| Layer | Technology |
|---|---|
| Language | Java 21+ (works unchanged on Java 25) |
| Build | Maven (WAR packaging) |
| Web | Jakarta Servlet 6, JSP, JSTL 3 |
| Server | Apache Tomcat **10.1.x** |
| Database | MySQL 8 |
| UI | Bootstrap 5, Bootstrap Icons, Chart.js, custom glassmorphism CSS |
| Architecture | MVC + DAO + Service layer, PreparedStatement everywhere |

## Features (15 modules)
1. **Project setup** - Maven MVC structure, landing page, DB connection pool-free JDBC utility
2. **Authentication** - register, login, logout, forgot-password (token flow), SHA-256 hashing, admin login, role-based filters, 30-min session timeout
3. **User dashboard** - widgets for medicines, reminders, diseases, allergies + recent safety checks
4. **User profile** - full CRUD incl. blood group, emergency contact, address and photo upload (2 MB)
5. **Disease management** - attach diseases (with diagnosis date) from the master list
6. **Medicine search** - by medicine / brand / generic name with pagination, autocomplete and full details view
7. **Current medicines** - CRUD with dose, frequency, duration, doctor, notes
8. **Drug Interaction Engine** - compares a candidate against every current medicine (green / yellow / red with reasons)
9. **Disease Warning Engine** - drug-vs-disease rules (heart, kidney, liver, pregnancy, ...)
10. **Allergy Checker** - scans medicine ingredients against the user's allergy list
11. **Reminders** - daily / weekly / monthly with pause & delete, shown on the dashboard
12. **History** - every safety check stored, searchable, exportable as CSV
13. **Admin panel** - manage users, medicines, diseases, interaction rules, warning rules (CRUD + search + pagination)
14. **Reports** - totals, verdict-distribution doughnut chart, most-checked-medicines bar chart
15. **Professional UI** - blue+green healthcare theme, glassmorphism cards, dark footer, spinner, toasts, animations, responsive

## Security
- `PreparedStatement` for every query (SQL-injection safe)
- SHA-256 password hashing (never plain text)
- JSTL `${...}` output + `InputValidator.escapeHtml` (XSS prevention)
- **CSRF protection**: synchronizer token issued at login; every POST under
  `/user/*` and `/admin/*` is rejected with 403 unless the hidden `csrf`
  field matches the session token
- `AuthFilter` (user area) and `AdminFilter` (admin area) enforce login + roles
- Session timeout 30 minutes; ownership checks on every delete/update

## Setup

### 1. Database
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample_data.sql
```
Edit `src/main/java/com/medguard/util/DBConnection.java` if your MySQL
username/password differ from `root` / `root`.

### 2. Build
```bash
mvn clean package
```
Produces `target/medguard.war`.

### 3. Deploy
Copy `target/medguard.war` into Tomcat 10.1's `webapps/` folder and start Tomcat.
Open **http://localhost:8080/medguard/**

## Demo credentials
| Role | Login | Password |
|---|---|---|
| User | `demo@medguard.com` | `user1234` |
| Admin | `admin` (at /admin/login.jsp) | `admin123` |

The demo user already has **Hypertension**, a **Penicillin allergy** and takes
**Warfarin** - so try a safety check on *Ibuprofen*, *Aspirin* or *Amoxicillin*
to see the engines fire.

## Project structure
```
src/main/java/com/medguard/
 ├── controller/   21 servlets (thin controllers)
 ├── dao/          10 DAOs - the ONLY classes that touch JDBC
 ├── service/      AuthService, SafetyService (3 engines), ReportService
 ├── model/        JavaBeans
 ├── filter/       Encoding, Auth (user), Admin (+CSRF on POST)
 ├── util/         DBConnection, PasswordUtil, CsrfUtil, AppConstants
 ├── validation/   InputValidator
 └── exception/    DataAccessException
src/main/webapp/
 ├── index.jsp, login.jsp, register.jsp, forgot.jsp, admin/login.jsp   (public)
 ├── WEB-INF/views/user/    9 protected user views (reachable only via servlet forward)
 ├── WEB-INF/views/admin/   7 protected admin views
 ├── WEB-INF/views/fragments/  shared head / navbars / footer
 ├── css/style.css, js/app.js
 └── uploads/               profile photos land here
database/  schema.sql + sample_data.sql
```

## Forgot-password note
There is no mail server in a college environment, so the generated reset link
is displayed on screen instead of being emailed. The token flow itself
(random token stored per user, single-use, cleared on reset) is identical to
production systems - swap the redirect for an email send and it's done.
