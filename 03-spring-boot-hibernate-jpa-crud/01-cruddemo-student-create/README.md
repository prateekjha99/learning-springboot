# Student Create — JPA/Hibernate

Creating records with JPA `EntityManager.persist()`.

---

## Theory

**JPA vs Hibernate**
- **JPA** (Jakarta Persistence API) — Specification; defines how Java objects map to database tables.
- **Hibernate** — Implementation of JPA. Spring Boot uses Hibernate under the hood when you add `spring-boot-starter-data-jpa`.

**ORM (Object-Relational Mapping)**
- Maps Java objects (entities) to database rows.
- You work with objects; Hibernate generates SQL (INSERT, SELECT, etc.) for you.

**EntityManager & Persistence Context**
- **EntityManager** — API to interact with the persistence layer. Think of it as a "session" for database operations.
- **Persistence Context** — In-memory cache of managed entities. When you `persist()`, the entity is added here; at transaction commit, Hibernate flushes changes to the DB.

**Transactions**
- Write operations (`persist`, `merge`, `remove`) must run inside a transaction.
- `@Transactional` starts a transaction; on method success it commits (and flushes to DB); on exception it rolls back.

---

## Prerequisites

MySQL + run `../00-starter-sql-scripts/` (create user, database, table).

---

## JPA Entity

```java
@Entity
@Table(name="student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="first_name")
    private String firstName;
    // ...
}
```

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Maps class to DB table |
| `@Table` | Table name (default: class name) |
| `@Id` | Primary key |
| `@GeneratedValue(IDENTITY)` | DB auto-increments ID |
| `@Column` | Column name mapping (camelCase → snake_case) |

---

## EntityManager — persist()

```java
@Repository
public class StudentDAOImpl implements StudentDAO {

    private EntityManager entityManager;

    @Autowired
    public StudentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Student theStudent) {
        entityManager.persist(theStudent);  // INSERT into DB
    }
}
```

- **`@Transactional`** — Required for write operations; JPA needs a transaction to persist.
- **`persist()`** — Adds entity to persistence context and inserts a row. After persist, `theStudent.getId()` is populated.

---

## EntityManager CRUD Methods

| Method | Operation |
|--------|------------|
| `persist()` | INSERT (this demo) |
| `find()` | SELECT by ID |
| `merge()` | UPDATE |
| `remove()` | DELETE |

---

## Run

```bash
./mvnw spring-boot:run
```

Creates 4 students on startup. Verify: `SELECT * FROM student_tracker.student;`
