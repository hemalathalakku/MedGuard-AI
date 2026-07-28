-- ============================================================
--  MedGuard AI - FULL Database Schema (all 15 modules)
--  Run once:  mysql -u root -p < database/schema.sql
-- ============================================================
DROP DATABASE IF EXISTS medguard;
CREATE DATABASE medguard CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medguard;

-- ---------------- Users ----------------
CREATE TABLE users (
    user_id           INT AUTO_INCREMENT PRIMARY KEY,
    full_name         VARCHAR(100) NOT NULL,
    email             VARCHAR(120) NOT NULL UNIQUE,
    password          VARCHAR(64)  NOT NULL,          -- SHA-256 hex hash
    age               INT,
    gender            ENUM('Male','Female','Other'),
    height_cm         DECIMAL(5,1),
    weight_kg         DECIMAL(5,1),
    blood_group       VARCHAR(5),
    emergency_contact VARCHAR(20),
    address           VARCHAR(255),
    photo             VARCHAR(255),                   -- filename under /uploads
    reset_token       VARCHAR(64),                    -- forgot-password token
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
);

-- ---------------- Admin ----------------
CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL
);

-- ---------------- Diseases master list ----------------
CREATE TABLE diseases (
    disease_id   INT AUTO_INCREMENT PRIMARY KEY,
    disease_name VARCHAR(100) NOT NULL UNIQUE,
    description  VARCHAR(255)
);

-- ---------------- User <-> disease (with history) ----------------
CREATE TABLE user_diseases (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    disease_id   INT NOT NULL,
    diagnosed_on DATE,
    CONSTRAINT fk_ud_user    FOREIGN KEY (user_id)    REFERENCES users(user_id)       ON DELETE CASCADE,
    CONSTRAINT fk_ud_disease FOREIGN KEY (disease_id) REFERENCES diseases(disease_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_disease (user_id, disease_id)
);

-- ---------------- User allergies ----------------
CREATE TABLE allergies (
    allergy_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    allergy_name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_al_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_allergies_user (user_id)
);

-- ---------------- Medicine catalogue ----------------
CREATE TABLE medicines (
    medicine_id          INT AUTO_INCREMENT PRIMARY KEY,
    medicine_name        VARCHAR(120) NOT NULL,
    generic_name         VARCHAR(120),
    brand                VARCHAR(120),
    manufacturer         VARCHAR(120),
    ingredients          TEXT,                        -- comma separated; allergy checker scans this
    description          TEXT,
    uses                 TEXT,
    dosage               TEXT,
    side_effects         TEXT,
    contraindications    TEXT,
    warnings             TEXT,
    pregnancy_category   VARCHAR(5),
    storage_instructions VARCHAR(255),
    age_restrictions     VARCHAR(120),
    INDEX idx_med_name (medicine_name),
    INDEX idx_med_generic (generic_name),
    INDEX idx_med_brand (brand)
);

-- ---------------- Medicines a user currently takes ----------------
CREATE TABLE current_medicines (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT NOT NULL,
    medicine_id   INT NOT NULL,
    dose          VARCHAR(60),
    frequency     VARCHAR(60),
    duration_days INT,
    doctor_name   VARCHAR(100),
    notes         VARCHAR(255),
    started_on    DATE,
    CONSTRAINT fk_cm_user FOREIGN KEY (user_id)     REFERENCES users(user_id)         ON DELETE CASCADE,
    CONSTRAINT fk_cm_med  FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_med (user_id, medicine_id)
);

-- ---------------- Drug-drug interaction rules ----------------
CREATE TABLE medicine_interactions (
    interaction_id INT AUTO_INCREMENT PRIMARY KEY,
    medicine_a_id  INT NOT NULL,
    medicine_b_id  INT NOT NULL,
    risk_level     ENUM('LOW','MODERATE','HIGH') NOT NULL,
    reason         TEXT,
    CONSTRAINT fk_mi_a FOREIGN KEY (medicine_a_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    CONSTRAINT fk_mi_b FOREIGN KEY (medicine_b_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    UNIQUE KEY uk_pair (medicine_a_id, medicine_b_id)
);

-- ---------------- Drug-disease warning rules ----------------
CREATE TABLE medicine_disease_warnings (
    warning_id  INT AUTO_INCREMENT PRIMARY KEY,
    medicine_id INT NOT NULL,
    disease_id  INT NOT NULL,
    severity    ENUM('CAUTION','NOT_RECOMMENDED') NOT NULL,
    reason      TEXT,
    CONSTRAINT fk_mdw_med     FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    CONSTRAINT fk_mdw_disease FOREIGN KEY (disease_id)  REFERENCES diseases(disease_id)   ON DELETE CASCADE,
    UNIQUE KEY uk_med_disease (medicine_id, disease_id)
);

-- ---------------- Safety-check history ----------------
CREATE TABLE medicine_history (
    history_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    medicine_id INT NOT NULL,
    result      ENUM('SAFE','CAUTION','DANGEROUS'),
    warnings    TEXT,                                 -- newline-separated reasons shown to the user
    checked_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mh_user FOREIGN KEY (user_id)     REFERENCES users(user_id)         ON DELETE CASCADE,
    CONSTRAINT fk_mh_med  FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    INDEX idx_mh_user (user_id)
);

-- ---------------- Medication reminders ----------------
CREATE TABLE reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    medicine_id INT NOT NULL,
    frequency   ENUM('DAILY','WEEKLY','MONTHLY') NOT NULL,
    remind_time TIME NOT NULL,
    detail      VARCHAR(60),                          -- e.g. "Monday" or "Day 15"
    active      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_r_user FOREIGN KEY (user_id)     REFERENCES users(user_id)         ON DELETE CASCADE,
    CONSTRAINT fk_r_med  FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE
);
