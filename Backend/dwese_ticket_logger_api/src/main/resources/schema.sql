DROP DATABASE IF EXISTS dwese_ticket_logger_webapp;
CREATE DATABASE dwese_ticket_logger_webapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dwese_ticket_logger_webapp;

CREATE TABLE IF NOT EXISTS region (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    image VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS province (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    region_id INT NOT NULL,
    CONSTRAINT fk_province_region
        FOREIGN KEY (region_id) REFERENCES region(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS supermarkets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    supermarket_id INT NOT NULL,
    province_id INT NOT NULL,
    CONSTRAINT fk_location_supermarket
        FOREIGN KEY (supermarket_id) REFERENCES supermarkets(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_location_province
        FOREIGN KEY (province_id) REFERENCES province(id)
        ON DELETE CASCADE
);

-- Crear la tabla 'users'
CREATE TABLE IF NOT EXISTS users (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) UNIQUE NOT NULL,
 password VARCHAR(100) NOT NULL,
 enabled BOOLEAN NOT NULL,
 first_name VARCHAR(50) NOT NULL,
 last_name VARCHAR(50) NOT NULL,
 image VARCHAR(255),
 created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 last_password_change_date TIMESTAMP
);

-- Crear la tabla 'roles'
CREATE TABLE IF NOT EXISTS roles (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(50) UNIQUE NOT NULL
);

-- Crear la tabla 'user_roles'
CREATE TABLE IF NOT EXISTS user_roles (
 user_id BIGINT NOT NULL,
 role_id BIGINT NOT NULL,
 PRIMARY KEY (user_id, role_id),
 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
 FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
