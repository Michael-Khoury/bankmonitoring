# Bank Transaction Monitoring System

A Java-based transaction-monitoring project combining a Spring Boot REST API, PostgreSQL storage, and Apache Spark batch analysis to identify potentially suspicious banking activity.

## Overview

The project contains two components:

- **Spring Boot backend:** Works with users, accounts, and transactions, with a service-layer rule for detecting repeated high-value activity.
- **Spark analysis:** Reads stored transactions from PostgreSQL through JDBC and filters high-value transactions for analysis.

Both components work with the same transaction data.

## Tech Stack

| Technology | Purpose |
| --- | --- |
| Java | Backend and analysis logic |
| Spring Boot | REST API and application framework |
| PostgreSQL | Relational data storage |
| Apache Spark | Batch transaction analysis |
| JDBC | Spark-to-PostgreSQL connectivity |
| Amazon RDS | Hosted PostgreSQL database used during development |
| Maven | Dependency management and builds |
| Postman | API testing |

## Backend Features

- REST API components for users, accounts, and transactions.
- Separate controller, model, repository, and service layers.
- PostgreSQL-backed persistence.
- Account-specific checks using transaction amounts and recent activity.
- A dedicated `FraudCheckService` for evaluating potentially suspicious transactions.

### Suspicious-Activity Rule

The `isSuspicious(Transaction newTxn)` method returns `true` when:

1. The incoming transaction amount is **greater than 1,000**.
2. At least **two stored transactions**:
   - Belong to the same account.
   - Have amounts greater than 1,000.
   - Have timestamps later than the current time minus one minute.

Otherwise, it returns `false`.

| Incoming Amount | Qualifying Stored Transactions | Result |
| --- | --- | --- |
| 500 | 2 | `false` |
| 1,000 | 2 | `false` |
| 1,500 | 1 | `false` |
| 1,500 | 2 | `true` |

The count depends on the records present when the check runs. If the incoming transaction has already been saved, it may be included in the count.

## Spark Analysis

The `FraudAnalysis` application performs batch analysis of stored transaction data.

### Processing Steps

1. Starts a Spark session.
2. Connects to PostgreSQL through JDBC.
3. Loads the `transaction` table into a Spark DataFrame.
4. Displays the schema and transaction records.
5. Filters transactions using:

   ```sql
   amount > 1000
   ```

6. Displays the matching records.
7. Stops the Spark session.

The current implementation uses `local[*]`, allowing Spark to use the available local CPU cores.

The Spark component applies an amount-based filter. The backend applies the separate account-and-time-window rule described above.

## Repository Organization

The repository contains the existing Spring Boot application alongside the Spark project.

| Component | Main Classes | Responsibility |
| --- | --- | --- |
| Backend controllers | `UserController`, `AccountController`, `TransactionController` | Handle API requests |
| Backend models | `User`, `Account`, `Transaction` | Represent domain data |
| Backend repositories | `UserRepository`, `AccountRepository`, `TransactionRepository` | Provide database access |
| Backend service | `FraudCheckService` | Evaluate suspicious transaction activity |
| Backend entry point | `BankMonitoringApplication` | Start the Spring Boot application |
| `spark-analysis/` | `FraudAnalysis` | Run batch transaction analysis |

The backend and Spark component maintain their own Maven configurations.

## Getting Started

### Prerequisites

- Java versions compatible with each component's `pom.xml`.
- A running PostgreSQL instance, locally or through Amazon RDS.
- Database connection settings configured for both components.
- Network access to the database.

Store database credentials in environment variables or local configuration excluded from version control.

### Run the Backend

From the Spring Boot directory containing `pom.xml` and the Maven wrapper files:

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

Alternatively, run `BankMonitoringApplication` directly from your IDE.

Use Postman to test the routes defined in the controller classes.

### Run Spark Analysis

1. Open `spark-analysis/pom.xml` as a Maven project in your IDE.
2. Load its dependencies and select a compatible JDK.
3. Configure the JDBC connection to the PostgreSQL database containing the transaction data.
4. Run the `main` method in `FraudAnalysis`.

The application prints transaction data and matching high-value transactions to the console.

## Implementation Notes

- The Spark job performs **batch processing**.
- Both components use **rule-based checks**.
- A matching transaction indicates potentially suspicious activity; it does not establish that fraud occurred.
- The backend currently filters records from `findAll()` using Java streams. Moving this filtering into a database query would reduce memory usage as transaction history grows.
- CSV export code is included in the Spark source but is currently commented out.

## Author

**Michael Khoury**

[GitHub](https://github.com/Michael-Khoury) · [Portfolio](https://michael-khoury.github.io)
