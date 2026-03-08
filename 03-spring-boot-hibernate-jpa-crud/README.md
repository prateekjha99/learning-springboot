# Spring Boot + JPA/Hibernate CRUD

CRUD operations using JPA and Hibernate with MySQL.

---

## Architecture: Spring → DAO → JPA → JDBC → Database

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Spring    │────►│     DAO     │────►│     JPA     │────►│    JDBC     │────►│  Database   │
│  (Controller│     │ (Data Access│     │ (Hibernate)│     │   Driver    │     │   (MySQL)   │
│  Service)   │     │   Object)   │     │ EntityMgr  │     │             │     │             │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
```

| Layer | Role |
|-------|------|
| **Spring** | Manages beans, DI, transactions. Your app (Controller, Service) calls DAO. |
| **DAO** | Data Access Object; encapsulates persistence. Uses EntityManager. Hides JPA details from business logic. |
| **JPA** | API + Hibernate implementation. EntityManager, entities, JPQL. Converts objects ↔ SQL. |
| **JDBC** | Java Database Connectivity; low-level API. Hibernate uses JDBC under the hood to talk to the DB. |
| **Database** | MySQL (or any supported DB). Stores rows. |

**Request flow:** Controller/Service → DAO.save(student) → EntityManager.persist() → Hibernate generates SQL → JDBC executes it → MySQL stores the row.

---

## JPA Concepts

**JPA vs Hibernate**
- **JPA** — Specification (Jakarta Persistence API); defines how Java objects map to databases.
- **Hibernate** — Implementation of JPA; Spring Boot uses it by default with `spring-boot-starter-data-jpa`.

**ORM (Object-Relational Mapping)**
- Maps Java entities to database rows. You work with objects; the provider generates SQL.

**EntityManager**
- Central API for persistence. Represents a "session" for database operations.
- Injected by Spring; used to persist, find, merge, and remove entities.

**Persistence Context**
- In-memory cache of managed entities. Tracks changes for dirty checking.
- Write operations are flushed to the DB when the transaction commits.

**Entity States**
- **New/Transient** — Not yet persisted.
- **Managed** — In persistence context; changes are tracked and synced.
- **Detached** — Was managed but no longer; changes are not auto-synced.
- **Removed** — Marked for deletion; row deleted on commit.

**Transactions**
- Write operations (`persist`, `merge`, `remove`) require a transaction.
- Read operations (`find`, queries) can run without one in many setups.
- `@Transactional` starts a transaction; commits on success, rolls back on exception.

**JPQL (Java Persistence Query Language)**
- Query language for JPA; uses entity and field names, not table/column names.
- Database-agnostic; translated to SQL by the provider.
- Supports `WHERE`, `ORDER BY`, named parameters, bulk updates/deletes.

**Schema Generation (ddl-auto)**
- Hibernate can create/update tables from entities.
- `update` — Creates missing tables/columns; keeps data. Good for development.
- `none` / `validate` — Use in production; prefer migrations (Flyway, Liquibase).

---

## Key Annotations

**JPA Entity annotations**

| Annotation | Where | Purpose |
|------------|-------|---------|
| `@Entity` | Class | Marks class as a JPA entity; maps to a DB table |
| `@Table(name="x")` | Class | Table name (default: class name) |
| `@Id` | Field | Primary key |
| `@GeneratedValue(strategy=IDENTITY)` | Field | DB auto-increments the ID |
| `@Column(name="x")` | Field | Column name (default: field name) |

**Example:**
```java
@Entity
@Table(name="student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="first_name")
    private String firstName;
}
```

**Spring DAO annotation**

| Annotation | Where | Purpose |
|------------|-------|---------|
| `@Repository` | Class | Spring stereotype for data access; enables exception translation, component scanning |

---

## EntityManager Methods

| Method | Purpose | When to use |
|--------|---------|-------------|
| **persist()** | INSERT new entity | Creating records. Entity must be new. |
| **find()** | SELECT by primary key | Lookup by ID. Returns `null` if not found. |
| **merge()** | UPDATE detached entity | Re-attach and sync changes. For entities loaded elsewhere. |
| **remove()** | DELETE entity | Entity must be managed. Load first, then remove. |
| **createQuery()** | Custom JPQL | `findAll`, `findByX`, bulk operations. Returns `TypedQuery`. |

**Query execution**
- `getResultList()` — Returns list; empty if no results.
- `getSingleResult()` — Returns one; throws if 0 or many.
- `executeUpdate()` — For JPQL UPDATE/DELETE; returns row count.

---
