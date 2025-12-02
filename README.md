# Supermarket Management System

## Overview
This is a **Java-based GUI application** that simulates a small supermarket management system.  
It allows management of **products, customers, billing, and reports**.  

The project is implemented using **Java Swing** for the GUI and a simple **repository pattern** for data management.

---

## Features

### 1. Products Management
- Add, view, and manage products.
- Each product has:
  - ID
  - Name
  - Category
  - Price
  - Quantity
- Low-stock alerts (future update possible).

### 2. Customers Management
- Add, view, and manage customers.
- Customers can have carts to add products for purchase.

### 3. Billing
- Add products to a customer’s cart.
- Checkout updates stock automatically.
- View the **final bill** before payment.
- Calculates total amount and can be extended with discounts.

### 4. Reports
- Generate product reports.
- Display stock quantity and categories.
- Can be extended to show sales reports and top-selling products.

### 5. Data Persistence
- Product and customer data can be saved and loaded from files.
- Allows data to persist even after closing the program.

---

## Installation & Running

1. **Requirements:**
   - Java JDK 11 or above
   - Any IDE (optional) or command-line compiler

2. **Running from Command Line:**

```bash
cd "D:\5th Semester\Oop\Final Project\src"
javac models\*.java repository\*.java utils\*.java Main.java
java Main
