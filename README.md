# 🎓 Student Record Management System

A modern **Java Swing** desktop application for managing student records with a sleek UI, secure authentication, and intelligent database interactions. Designed with an emphasis on **custom UI components**, **best practices**, and **modular architecture**, this system provides all the necessary features to handle student data in a professional and intuitive manner.

---

## 🎨 Beautiful & Customizable UI

This application redefines the look of traditional Java Swing by introducing:

* ✨ **Rounded UI Components**: Text fields, buttons, combo boxes
* ✨ **Gradient Table Headers**: Enhances readability and visual appeal
* ✨ **Custom Buttons**:

  * Rounded corners
  * Hover effects (hand cursor)
  * Custom paint logic for modern appearance
* ✨ **Dialog Enhancements**:

  * Custom confirmation and message dialogs
  * Icons and styled messages for consistency
* ✨ **Curved Table Containers**:

  * Tables are rendered inside a rounded card with smooth scroll behavior
  * Auto-resizing columns to fit content

All UI behaviors are reusable through a central utility system, supporting scalability and maintainability.

---

## 🔍 Advanced Front-End Features

* ✅ **Live Search Engine**

  * Search by ID, name, gender, course, year level, or email
  * Flexible input: "2nd", "second year", or "2" interpreted the same

* ✅ **Interactive Dashboard**

  * Displays current logged-in admin
  * Contains actions for adding, editing, searching, and deleting students

* ✅ **Inline Field Editing**

  * Allows real-time updates to specific student fields
  * Smart validation and matching to avoid unintentional changes

* ✅ **Record Table**

  * Styled data table with dynamic updates
  * Gradient headers, centered text, adaptive column width

---

## 🔗 Robust Database Integration

* 📚 **Database**: MySQL (configured via XAMPP)
* 🌐 **Dynamic Configuration**: Loaded through external `db.properties` file
* ⚡ **JDBC + MySQL Connector** for fast and secure database access
* ✉️ **Modular Queries**: Each action (insert, update, delete, search) encapsulated in reusable logic
* ⏰ **Audit Trail**:

  * Logs all login/logout actions with timestamp, admin ID, and username

---

## 🧬 Algorithmic Data Management

* 🌐 **ID Generation**:

  * Student IDs follow format: `SYYYYNNNNN` (e.g., `S202500001`)
  * Admin IDs follow format: `AYYYYNNNNN` (e.g., `A202500001`)
  * Ensures uniqueness by querying last used ID with matching prefix

* 📧 **Email Generator**:

  * Automatically generates school emails like `firstname_lastname@dlsl.edu.ph`
  * Lowercases, trims, and sanitizes names to fit standard format

* ⚖️ **Flexible Search Matching**:

  * Numeric string? Interpreted as age or year level
  * Words like "first" or "1st"? Parsed as year level
  * Uses LIKE and exact matches for robust query resolution

---

## 🏋️‍♂️ Admin & Session Control

* 🔐 **Admin Registration with System Password**

  * Before an admin can register, they must input a special **System Password**
  * This System Password is stored in the database in a **hashed format using jBCrypt**
  * The registration proceeds **only if the entered password matches the hashed system password**, ensuring that only trusted users can add new admins
  * Prevents unauthorized users from creating new admin accounts, adding an extra layer of protection

* 🔑 **Secure Login**

  * Admin passwords are hashed using **jBCrypt**
  * Authentication checks compare hashed inputs to stored values

* ☑️ **Session Handling**

  * Admin login state is stored in session
  * Returns users to dashboard if already logged in

---

## ✅ Best Practices Followed

* 📊 **Separation of Concerns**:

  * UI, DB, logic, session, and utility classes clearly divided

* 🌐 **External Configuration**:

  * DB credentials separated into a config file for easy modification

* ✨ **Code Readability**:

  * Proper JavaDoc and inline comments
  * Clean formatting and reusable methods

* 🔁 **Reusable UI Components**

  * All UI rendering abstracted into `SwingUtility`

* ⛓ **Safe Data Handling**:

  * Parameterized queries to prevent SQL injection
  * Data type checks and validations before DB actions

---

## 🚀 How to Get Started

1. Install and run **XAMPP**, then create your MySQL database and tables.
2. Clone this repository to your local machine.
3. Configure your database in `config/db/db.properties`:

   ```properties
   db.url=jdbc:mysql://localhost:3306/student_db
   db.user=root
   db.password=
   ```
4. Run the app via your Java IDE.
5. SQL Commands

CREATE TABLE admin_accounts (
  adminID INT AUTO_INCREMENT PRIMARY KEY,
  adminUsername VARCHAR(50) NOT NULL UNIQUE,
  adminPassword VARCHAR(225) NOT NULL
);

CREATE TABLE history_logs (
  logID INT AUTO_INCREMENT PRIMARY KEY,
  adminID VARCHAR(20) NOT NULL,
  adminUsername VARCHAR(50) NOT NULL,
  loginTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_records (
  studentID VARCHAR(20) PRIMARY KEY,
  firstName VARCHAR(50) NOT NULL,
  lastName VARCHAR(50) NOT NULL,
  middleName VARCHAR(50),
  age INT CHECK (age >= 0),
  gender VARCHAR(1) NOT NULL,
  course VARCHAR(100) NOT NULL,
  yearLevel INT CHECK (yearLevel >= 1),
  school_email VARCHAR(100) UNIQUE NOT NULL
);
