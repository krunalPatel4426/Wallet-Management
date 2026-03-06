# Enterprise Wallet & Order Fulfillment System

## 📌 Project Overview
This project is a robust, simple transaction system built for enterprise product development. [cite_start]It maintains a secure digital wallet for clients, allowing administrators to manage balances and clients to place orders[cite: 10, 11]. The system emphasizes data consistency, reliable external API integration, and concurrent request handling.

## ✨ Key Features
* [cite_start]**Atomic Transactions:** Wallet deductions and order creations are handled atomically to ensure data integrity[cite: 30, 31]. 
* **Independent Audit Logging:** Failed transactions (e.g., due to insufficient funds or concurrent access) are logged using independent transaction boundaries (`Propagation.REQUIRES_NEW`) to ensure audit trails are never lost during a rollback.
* [cite_start]**External Fulfillment Integration:** Automatically communicates with an external fulfillment API upon successful order creation and stores the resulting fulfillment ID[cite: 32, 33].
* **Concurrency Control:** Implements Optimistic Locking (via entity versioning) to safely handle simultaneous wallet update requests.
* [cite_start]**Role-Based API Separation:** Distinct endpoints for Admin operations (credit/debit) and Client operations (orders/balance)[cite: 14, 19, 24].

## 🛠️ Tech Stack
* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Data Access:** Spring Data JPA / Hibernate
* **Database:** H2 Database (In-Memory for easy testing) / PostgreSQL
* **Validation:** Spring Boot Starter Validation
* **External Calls:** Spring `RestClient` / `WebClient`

## 🏗️ Architectural Decisions & Design Patterns
1.  **Separation of Concerns:** Business logic is decoupled from controllers. A dedicated `HelperService` (or `TransactionLogService`) handles isolated database operations, preventing Spring AOP proxy self-invocation issues.
2.  **Precise Financial Calculations:** All monetary values are handled using `BigDecimal` to prevent the precision loss associated with floating-point arithmetic.
3.  [cite_start]**Global Exception Handling:** A `@ControllerAdvice` implementation intercepts custom exceptions (e.g., `UserNotFoundException`, `InsufficientBalanceException`) to return standardized, clean JSON error responses[cite: 7].

## 🚀 API Endpoints

### Admin APIs
#### 1. Credit Wallet
[cite_start]Credits a specified amount to a client's wallet and creates a ledger entry[cite: 16, 18].
* [cite_start]**URL:** `POST /admin/wallet/credit` [cite: 15]
* **Body:**
    ```json
    {
      "client_id": 1,
      "amount": 500.00
    }
    ```

#### 2. Debit Wallet
[cite_start]Debits an amount if the balance is sufficient and logs the transaction[cite: 21, 23].
* [cite_start]**URL:** `POST /admin/wallet/debit` [cite: 20]
* **Body:**
    ```json
    {
      "client_id": 1,
      "amount": 100.00
    }
    ```

### Client APIs
[cite_start]*Note: Client APIs require a `client-id` header for identification[cite: 26, 41, 45].*

#### 3. Create Order
[cite_start]Validates balance, deducts funds atomically, creates an order, and calls the external fulfillment API[cite: 29, 30, 31, 32].
* [cite_start]**URL:** `POST /orders` [cite: 25]
* [cite_start]**Headers:** `client-id: <id>` [cite: 26]
* **Body:**
    ```json
    {
      "amount": 150.00
    }
    ```

#### 4. Get Order Details
[cite_start]Returns order information including amount, status, and fulfillment ID[cite: 42].
* [cite_start]**URL:** `GET /orders/{order_id}` [cite: 40]
* [cite_start]**Headers:** `client-id: <id>` [cite: 41]

#### 5. Get Wallet Balance
[cite_start]Returns the current wallet balance for the client[cite: 46].
* [cite_start]**URL:** `GET /wallet/balance` [cite: 44]
* [cite_start]**Headers:** `client-id: <id>` [cite: 45]

## ⚙️ Setup and Installation
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/krunalPatel4426/Wallet-Management.git
    cd wallet-order-system
    ```
2.  **Build the project:**
    ```bash
    mvn clean install
    ```
3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    *The application will start on `http://localhost:8080`.*
